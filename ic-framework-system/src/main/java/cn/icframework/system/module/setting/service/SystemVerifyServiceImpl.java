package cn.icframework.system.module.setting.service;

import cn.icframework.auth.standard.ISystemVerifyService;
import cn.icframework.core.utils.MD5Util;
import cn.icframework.core.utils.RsaUtils;
import cn.icframework.system.module.setting.Setting;
import cn.icframework.system.module.setting.pojo.vo.ActivationInfoVO;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author hzl
 * @date 2022/7/5
 */
@Slf4j
@Service
public class SystemVerifyServiceImpl extends ISystemVerifyService {

    @Value("${app.rsa-public:}")
    private String RSA_PUBLIC;
    @Value("${app.grant:}")
    private String GRANT;
    @Resource
    @Lazy
    private SettingService settingService;

    @Override
    public StatusEnum validate() {
        if (getStatus() != null) {
            return getStatus();
        } else {
            updateStatus();
        }
        return getStatus();
    }

    /**
     * 一个小时刷新一次
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void updateStatus() {
        Setting one = settingService.selectOne();
        if (one == null) {
            setStatus(StatusEnum.UNINITIALIZED);
            return;
        }
        try {
            String activationCode = one.getActivationCode();
            ActivationInfoVO activationInfoVO = JSONObject.parseObject(RsaUtils.publicDecrypt(activationCode, RSA_PUBLIC), ActivationInfoVO.class);
            String md5Code = activationInfoVO.getMd5Code();
            String encode = MD5Util.encode(md5Code);
            if (activationInfoVO.getMd5Hash().equals(encode) && GRANT.equals(activationInfoVO.getGrant())) {
                if (activationInfoVO.getForever() || activationInfoVO.getOutTime() > System.currentTimeMillis()) {
                    setStatus(StatusEnum.SUCCESS);
                } else {
                    setStatus(StatusEnum.OUT_DATE);
                }
                return;
            }
        } catch (Exception e) {
            log.error("系统校验失败", e);
        }
        setStatus(StatusEnum.NOT_ACTIVE);
    }
}
