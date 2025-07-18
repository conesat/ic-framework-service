package cn.icframework.system.module.setting.pojo.vo;

import cn.icframework.system.enums.AdType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author create by ic gen
 * @since 2023/06/17
 */
@Getter
@Setter
public class SettingVO implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    /**
     * 系统名称
     */
    private String name;
    /**
     * logo
     */
    private String logoFileId;
    /**
     * 域名
     */
    private String domain;

    /**
     * 激活时间
     */
    private LocalDateTime activateTime;
    /**
     * 过期时间
     */
    private LocalDateTime outDateTime;

    private ActivationInfoVO activationInfoVO;

    /**
     * 首页广告文件地址
     */
    private String adFileUrl;

    /**
     * 首页广告跳转地址
     */
    private String adUrl;

    /**
     * 首页广告类型
     */
    private AdType adType;

    /**
     * App开屏广告文件地址
     */
    private String appAdFileUrl;

    /**
     * App开屏广告跳转地址
     */
    private String appAdUrl;

    /**
     * App开屏广告类型
     */
    private AdType appAdType;
}
