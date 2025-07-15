package cn.icframework.hotel.module.roomconsumeorder.pojo.dto;

import java.lang.Integer;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.icframework.hotel.common.enums.RoomConsumeOrderStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomConsumeOrderDTO {
    private Long id;
    /**
     * 所属订单
     */
    private Long roomOrderId;
    /**
     * 项目id
     */
    private Long customerItemId;
    /**
     * 金额
     */
    private Double price;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 状态
     */
    private RoomConsumeOrderStatus status;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
}
