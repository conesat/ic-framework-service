package cn.icframework.system.module.setting.pojo.dto;

import cn.icframework.system.enums.AdType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author create by ic gen
 * @since 2023/06/17
 */
@Getter
@Setter
public class SettingDTO {
    /**
     * 系统名称
     */
    @NotNull
    private String name;
    /**
     * logo
     */
    private String logoFileId;
    /**
     * 域名
     */
    @NotNull
    private String domain;

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
