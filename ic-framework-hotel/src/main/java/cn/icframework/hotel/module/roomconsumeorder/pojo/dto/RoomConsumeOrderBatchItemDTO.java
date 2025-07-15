package cn.icframework.hotel.module.roomconsumeorder.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomConsumeOrderBatchItemDTO {
    /**
     * 项目id
     */
    private Long customerItemId;
    /**
     * 数量
     */
    private Integer num;
}
