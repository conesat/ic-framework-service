package cn.icframework.hotel.module.roomconsumeorder.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomConsumeOrderBatchDTO {
    /**
     * 所属订单
     */
    private Long roomOrderId;
    /**
     * 项目
     */
    private RoomConsumeOrderBatchItemDTO[] items;
}
