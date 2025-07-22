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
import cn.icframework.core.utils.IpUtils;
import cn.icframework.core.utils.LocalDateTimeUtils;
import cn.icframework.system.common.RegisterLoginHelper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.iplock.IpLock;
import cn.icframework.system.module.iplock.service.IpLockService;
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
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author ic
 * @since 2024/09/11
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
    private final IpLockService ipLockService;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(OnlineUserDTO dto) {
        OnlineUser entity = dto.getSessionId() != null ? selectById(dto.getSessionId()) : new OnlineUser();
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
     * @param username    账号
     * @param passwd      密码
     * @param verifyCode  验证码
     * @param captchaCode 验证码code
     * @return
     */
    public UserLoginInfo login(HttpServletRequest request,
                               String username,
                               String passwd,
                               String verifyCode,
                               String captchaCode,
                               Integer refreshTokenTimOut) {
        return login(request, username, passwd, verifyCode, captchaCode, refreshTokenTimOut, false);
    }

    public UserLoginInfo login(HttpServletRequest request,
                               String username,
                               String passwd,
                               String verifyCode,
                               String captchaCode,
                               Integer refreshTokenTimOut,
                               Boolean app) {
        String ipAddress = IpUtils.getIpAddress(request);
        IpLock ipLock = ipLockService.selectById(ipAddress);

        if (ipLock != null && ipLock.getLockEndTime().isAfter(LocalDateTime.now())) {
            Assert.isTrue(ipLock.getLoginFailCount() < IpLock.MAX_LOGIN_FAIL_COUNT, String.format("账号锁定，请稍%s后再试", LocalDateTimeUtils.getFormatSeconds(
                    Duration.between(LocalDateTime.now(), ipLock.getLockEndTime()).getSeconds())));
            if (ipLock.getLoginFailCount() >= 3 && !app) {
                Assert.isNotEmpty(verifyCode, "验证码不能为空");
                // 校验验证码
                String codeCache = registerLoginHelper.getCaptcha(UserType.SYSTEM_USER, captchaCode);
                Assert.isFalse(!Objects.equals(codeCache, verifyCode), "验证码错误");
            }
        } else if (StringUtils.hasLength(verifyCode)) {
            // 校验验证码
            String codeCache = registerLoginHelper.getCaptcha(UserType.SYSTEM_USER, captchaCode);
            Assert.isFalse(!Objects.equals(codeCache, verifyCode), "验证码错误");
        }

        UserDef def = UserDef.table();
        User user = userService.selectOne(def.username.eq(username));
        if (user == null) {
            handleLock(ipLock, ipAddress);
            throw new RuntimeException("账号或密码有误");
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
            handleLock(ipLock, ipAddress);
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
        if (ipLock != null) {
            ipLockService.deleteById(ipLock.getIp());
        }
        return loginInfo;
    }

    private void handleLock(IpLock ipLock, String ipAddress) {
        if (ipLock != null) {
            ipLock.setLoginFailCount(ipLock.getLoginFailCount() + 1);
            if (ipLock.getLoginFailCount() >= IpLock.MAX_LOGIN_FAIL_COUNT) {
                ipLock.setLockEndTime(LocalDateTime.now().plusMinutes(IpLock.LOCK_MINUTES));
                ipLockService.updateById(ipLock);
                throw new RuntimeException("账号或密码有误，账号锁定请10分钟后重试");
            } else {
                ipLockService.updateById(ipLock);
            }
        } else {
            ipLock = new IpLock();
            ipLock.setIp(ipAddress);
            ipLock.setLoginFailCount(1);
            ipLock.setLockEndTime(LocalDateTime.now().plusMinutes(IpLock.LOCK_MINUTES));
            ipLock.setFirstFailTime(LocalDateTime.now());
            ipLockService.insert(ipLock);
        }
    }


    public TokenInfo refreshToken() {
        return JWTUtils.refreshToken();
    }

    /**
     * 调用 JWTUtils.createToken 会触发这个方法
     * 记录登录信息
     *
     * @param onlineInfo 登录信息
     */
    @Override
    public void login(OnlineInfo onlineInfo) {
        OnlineUser onlineUser = new OnlineUser();
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

    /**
     * 检查token是否有效 如果不允许使用，请抛出对应异常
     *
     * @param userId    用户ID
     * @param sessionId sessionId
     */
    @Override
    public void verify(String userId, Long sessionId) {

    }

    /**
     * 强制退出
     *
     * @param ids 用户id
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
