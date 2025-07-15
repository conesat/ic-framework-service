package cn.icframework.hotel.module.roomorder.pojo.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomOrderDTO {
    private Long id;
    /**
     * 客户-用户id
     */
    private Long userId;
    /**
     * 客户姓名
     */
    @NotEmpty
    private String customerName;
    /**
     * 客户电话
     */
    @NotEmpty
    private String customerPhone;
    /**
     * 房间价格
     */
    private Double roomPrice;
    /**
     * 实付价格
     */
    private Double actualRoomPrice;
    /**
     * 入住时间
     */
    @Nonnull
    private LocalDate inDate;
    /**
     * 离店时间
     */
    @Nonnull
    private LocalDate outDate;
    /**
     * 房间号
     */
    @Nonnull
    private Long roomId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否已支付
     */
    private Boolean payed;
    /**
     * 是否已入住
     */
    private Boolean checkIn;

}
