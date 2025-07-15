package cn.icframework.system.module.setting.service;

import cn.icframework.common.enums.Status;
import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.common.exception.MessageException;
import cn.icframework.core.common.helper.I18N;
import cn.icframework.auth.standard.ISystemVerifyService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.core.utils.MD5Util;
import cn.icframework.core.utils.RsaUtils;
import cn.icframework.system.consts.I18NKeys;
import cn.icframework.system.consts.R;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.pojo.dto.UserDTO;
import cn.icframework.system.module.user.service.UserService;
import cn.icframework.system.module.role.Role;
import cn.icframework.system.module.role.def.RoleDef;
import cn.icframework.system.module.role.service.RoleService;
import cn.icframework.system.module.setting.Setting;
import cn.icframework.system.module.setting.dao.SettingMapper;
import cn.icframework.system.module.setting.pojo.dto.InitDTO;
import cn.icframework.system.module.setting.pojo.dto.SettingDTO;
import cn.icframework.system.module.setting.pojo.vo.ActivationInfoVO;
import cn.icframework.system.module.setting.pojo.vo.SettingVO;
import cn.icframework.system.module.setting.pojo.vo.SettingVOConverter;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author hzl
 * @date 2023/6/5
 */
@Service
@RequiredArgsConstructor
public class SettingService extends BasicService<SettingMapper, Setting> {
    @Value("${app.rsa-public}")
    private String RSA_PUBLIC;
    @Value("${app.grant}")
    private String GRANT;
    private final ISystemVerifyService systemVerifyServiceImpl;
    private final UserService userService;
    private final RoleService roleService;
    private final SettingVOConverter settingVOConverter;


    /**
     * 系统激活
     *
     * @param activationCode
     * @return
     */
    public ActivationInfoVO activitySystem(String activationCode) {
        try {
            ActivationInfoVO activationInfoVO = decryptCode(activationCode);
            String md5Code = activationInfoVO.getMd5Code();
            String encode = MD5Util.encode(md5Code);
            Assert.isEquals(activationInfoVO.getMd5Hash(), encode, I18N.g(I18NKeys.SYSTEM_ACTIVATION_CODE_ERROR));
            Assert.isEquals(GRANT, activationInfoVO.getGrant(), I18N.g(I18NKeys.SYSTEM_ACTIVATION_CODE_ERROR));
            Assert.isTrue(activationInfoVO.getForever() || activationInfoVO.getOutTime() > System.currentTimeMillis(), I18N.g(I18NKeys.SYSTEM_ACTIVATION_OUT_TIME));
            systemVerifyServiceImpl.setStatus(ISystemVerifyService.StatusEnum.SUCCESS);
            return activationInfoVO;
        } catch (Exception e) {
            if (e instanceof MessageException) {
                throw (MessageException) e;
            }
            throw new MessageException(I18N.g(I18NKeys.SYSTEM_ACTIVATION_CODE_ERROR));
        }
    }

    /**
     * 系统激活
     *
     * @param activationCode
     * @return
     */
    public ActivationInfoVO decryptCode(String activationCode) {
        try {
            return JSONObject.parseObject(RsaUtils.publicDecrypt(activationCode, RSA_PUBLIC), ActivationInfoVO.class);
        } catch (Exception e) {
            if (e instanceof MessageException) {
                throw (MessageException) e;
            }
            throw new MessageException(I18N.g(I18NKeys.SYSTEM_ACTIVATION_CODE_ERROR));
        }
    }

    /**
     * 系统初始化
     *
     * @param initDTO
     * @return
     */
    @Transactional
    public ActivationInfoVO updateActivationCode(String code) {
        //系统激活
        ActivationInfoVO activationInfoVO = activitySystem(code);
        Setting setting = selectOne();
        if (setting == null) {
            setting = Setting.def();
            setting.setName("");
            setting.setDomain("");
            setting.setActivateTime(LocalDateTime.now());
            setting.setOutDateTime(Instant.ofEpochMilli(activationInfoVO.getOutTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            setting.setActivationCode(code);
            insert(setting);
        } else {
            setting.setActivationCode(code);
            updateById(setting);
        }
        return activationInfoVO;
    }

    /**
     * 系统初始化
     *
     * @param initDTO
     * @return
     */
    @Transactional
    public ActivationInfoVO init(InitDTO initDTO) {
        //系统激活
        String activationCode = initDTO.getActivationCode();
        ActivationInfoVO activationInfoVO = activitySystem(activationCode);
        Setting setting = selectOne();
        if (setting == null) {
            setting = Setting.def();
            setting.setName(initDTO.getName());
            setting.setDomain(initDTO.getUrl());
            setting.setActivateTime(LocalDateTime.now());
            setting.setOutDateTime(Instant.ofEpochMilli(activationInfoVO.getOutTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            setting.setActivationCode(initDTO.getActivationCode());
            insert(setting);
        } else {
            setting.setActivationCode(initDTO.getActivationCode());
            updateById(setting);
        }
        //超管
        User user = userService.selectOne();
        if (user == null) {
            Role adminRole = roleService.selectOne(RoleDef.table().su.eq(true));
            if (adminRole == null) {
                adminRole = Role.def();
                adminRole.setSu(true);
                adminRole.setSystem(true);
                adminRole.setUserType(UserType.SYSTEM_USER);
                adminRole.setName("超级管理员");
                adminRole.setSign(R.R_ADMIN);
                roleService.insert(adminRole);
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setName("超级管理员");
            userDTO.setUsername(initDTO.getUsername());
            userDTO.setPasswd(initDTO.getPasswd());
            userDTO.setStatus(Status.ENABLE);
            userDTO.setRoleIds(new Long[]{adminRole.getId()});
            userService.edit(userDTO);
        }
        return activationInfoVO;
    }

    @Cacheable(cacheNames = "setting")
    public SettingVO detail() {
        Setting setting = selectOne();
        if (setting == null) {
            return new SettingVO();
        }
        SettingVO vo = settingVOConverter.convert(setting);
        vo.setActivationInfoVO(decryptCode(setting.getActivationCode()));
        return vo;
    }


    @CacheEvict(value = "setting")
    public void removeCache() {
    }

    @Transactional
    public void edit(SettingDTO form) {
        Setting entity = selectOne();
        BeanUtils.copyExcludeProps(form, entity);
        updateById(entity);
    }
}
