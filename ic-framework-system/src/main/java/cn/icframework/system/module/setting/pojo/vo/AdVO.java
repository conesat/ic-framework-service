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
public class AdVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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

}
