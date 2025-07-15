package cn.icframework.system.module.setting.pojo.vo;

import cn.icframework.system.enums.AdType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author create by ic gen
 * @date 2023/06/17
 */
@Getter
@Setter
public class AppAdVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
