package cn.icframework.system.module.onlineuser.service;

import cn.icframework.auth.config.IcJwtConfig;
import cn.icframework.auth.entity.UserProps;
import cn.icframework.auth.standard.IOnlineUserService;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.common.consts.TokenInfo;
import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.common.exception.TokenOutTimeException;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.core.utils.LocalDateTimeUtils;
import cn.icframework.system.common.RegisterLoginHelper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.onlineuser.OnlineUser;
import cn.icframework.system.module.onlineuser.dao.OnlineUserMapper;
import cn.icframework.system.module.onlineuser.def.OnlineUserDef;
import cn.icframework.system.module.onlineuser.pojo.dto.OnlineUserDTO;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.service.SysFileService;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.user.pojo.vo.UserLoginInfo;
import cn.icframework.system.module.user.service.UserService;
import cn.icframework.system.module.userrole.service.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author ic
 * @date 2024/09/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OnlineUserService extends BasicService<OnlineUserMapper, OnlineUser> implements IOnlineUserService {

    private final SysFileService sysFileService;
    private final RegisterLoginHelper registerLoginHelper;
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final IcJwtConfig icJwtConfig;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(OnlineUserDTO dto) {
        OnlineUser entity = dto.getSessionId() != null ? selectById(dto.getSessionId()) : OnlineUser.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getSessionId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }


    /**
     * 构建登录信息
     *
     * @param username 账号
     * @param passwd   密码
     * @param code     验证码
     * @return
     */
    @Transactional
    public UserLoginInfo login(HttpServletRequest request, String username, String passwd, String code, Integer refreshTokenTimOut) {
        UserDef def = UserDef.table();
        User user = userService.selectOne(def.username.eq(username));
        Assert.isNotNull(user, "账号或密码有误");

        // 登录失败超过三次 且最后一次错误时间在十分钟内，必须填写验证码
        if (user.getLoginFailCount() > 3
                && LocalDateTime.now().minusMinutes(10).isAfter(user.getLastLoginFailTime())) {
            Assert.isNotEmpty(code, "验证码不能为空");
            // 校验验证码
            String codeCache = registerLoginHelper.getCaptcha(UserType.SYSTEM_USER, username);
            Assert.isFalse(!Objects.equals(codeCache, code), "验证码错误");
        }

        // 校验输入密码与数据库密码是否一致
        String inputPassword;
        try {
            inputPassword = registerLoginHelper.decryptPassWd(UserType.SYSTEM_USER, username, passwd);
        } catch (Exception e) {
            log.error("解密失败", e);
            throw new RuntimeException("登录失败");
        }
        if (!Objects.equals(inputPassword, user.getPasswd())) {
            user.setLoginFailCount(user.getLoginFailCount() + 1);
            userService.updateById(user);
            throw new RuntimeException("账号或密码有误");
        }

        UserProps userProps = new UserProps();
        userProps.setRequest(request);
        userProps.setUserType(UserType.SYSTEM_USER);
        userProps.setUsername(user.getUsername());
        userProps.setUserId(user.getId());
        userProps.setSu(userRoleService.isSu(user.getId()));
        TokenInfo token = JWTUtils.createToken(userProps,
                icJwtConfig.getTimeout(),
                refreshTokenTimOut == null ? icJwtConfig.getTimeout() * 10 : refreshTokenTimOut);

        UserLoginInfo loginInfo = new UserLoginInfo();
        loginInfo.setToken(token.getToken());
        loginInfo.setRefreshToken(token.getRefreshToken());
        loginInfo.setId(user.getId());
        loginInfo.setUsername(username);
        loginInfo.setName(user.getName());
        if (user.getAvatarFileId() != null) {
            SysFile sysFile = sysFileService.selectById(user.getAvatarFileId());
            loginInfo.setAvatarFileUrl(sysFile.getBucketUrl() + "/" + sysFile.getOssObjectName());
        }
        return loginInfo;
    }


    public TokenInfo refreshToken() {
        return JWTUtils.refreshToken();
    }

    @Override
    public void login(OnlineInfo onlineInfo) {
        OnlineUser onlineUser = OnlineUser.def();
        onlineUser.setSessionId(onlineInfo.getSessionId());
        onlineUser.setUserId((Long) onlineInfo.getUserId());
        onlineUser.setLoginTime(LocalDateTimeUtils.parse(onlineInfo.getLoginTime()));
        onlineUser.setExpireTime(LocalDateTimeUtils.parse(onlineInfo.getExpireTime()));
        onlineUser.setIp(onlineInfo.getIp());
        onlineUser.setLocation(onlineInfo.getLocation());
        onlineUser.setSystem(onlineInfo.getSystem());
        onlineUser.setBrowser(onlineInfo.getBrowser());
        onlineUser.setPlatform(onlineInfo.getPlatform());
        onlineUser.setUserType(onlineInfo.getUserType());
        insert(onlineUser);
    }

    @Override
    public void refresh(Long sessionId, long timeOut) {
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setSessionId(sessionId);
        onlineUser.setExpireTime(LocalDateTimeUtils.parse(timeOut));
        int i = updateById(onlineUser);
        if (i == 0) {
            throw new TokenOutTimeException();
        }
    }

    @Override
    public void verify(String userId, Long sessionId) {

    }

    /**
     * 强制退出
     *
     * @param ids
     */
    public void logout(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }
        for (Long id : ids) {
            removeSessionId(id);
        }
        deleteByIds(ids);
    }

    public void logout(List<Long> ids, Long userId) {
        OnlineUserDef onlineUserDef = OnlineUserDef.table();
        List<Long> mineIds = select(SELECT(onlineUserDef.sessionId).FROM(onlineUserDef).WHERE(onlineUserDef.sessionId.in(ids).userId.eq(userId)), Long.class);
        logout(mineIds);
    }

    public void logout(Long sessionId) {
        removeSessionId(sessionId);
        deleteById(sessionId);
    }

}
