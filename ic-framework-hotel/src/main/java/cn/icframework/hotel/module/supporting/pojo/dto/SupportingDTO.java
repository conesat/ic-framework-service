package cn.icframework.hotel.module.supporting.pojo.dto;

import cn.icframework.hotel.common.enums.SupportingTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class SupportingDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标文件id
     */
    private Long iconFileId;
    /**
     * 配套设施类型
     */
    private SupportingTypeEnum supportingType;

}
