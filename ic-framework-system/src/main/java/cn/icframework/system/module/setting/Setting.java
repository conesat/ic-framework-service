package cn.icframework.system.module.setting;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.system.enums.AdType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author hzl
 * @since 2023/6/5
 */
@Getter
@Setter
@Table(value = "sys_setting", comment = "系统设置")
public class Setting {
    @Id
    private long id;

    /**
     * 系统名称
     */
    @NotEmpty
    @TableField(notNull = true, length = 30)
    private String name;

    /**
     * 首页广告文件地址
     */
    @TableField(comment = "首页广告文件地址", length = 255)
    private String adFileUrl;

    /**
     * 首页广告跳转地址
     */
    @TableField(comment = "首页广告跳转地址", length = 255)
    private String adUrl;

    /**
     * 首页广告类型
     */
    @TableField(comment = "首页广告类型")
    private AdType adType;

    /**
     * App开屏广告文件地址
     */
    @TableField(comment = "App开屏广告文件地址", length = 255)
    private String appAdFileUrl;

    /**
     * App开屏广告跳转地址
     */
    @TableField(comment = "App开屏广告跳转地址", length = 255)
    private String appAdUrl;

    /**
     * App开屏广告类型
     */
    @TableField(comment = "App开屏广告类型")
    private AdType appAdType;

    /**
     * 系统logo文件id
     */
    @TableField(comment = "logo")
    private String logoFileId;

    /**
     * 域名
     */
    @NotEmpty
    @TableField(comment = "域名", notNull = true)
    private String domain;

    /**
     * 激活码
     */
    @NotEmpty
    @TableField(notNull = true, length = 1000, comment = "激活码")
    private String activationCode;

    /**
     * 激活时间
     */
    @TableField(comment = "激活时间")
    private LocalDateTime activateTime;

    /**
     * 过期时间
     */
    @TableField(comment = "过期时间")
    private LocalDateTime outDateTime;
}
