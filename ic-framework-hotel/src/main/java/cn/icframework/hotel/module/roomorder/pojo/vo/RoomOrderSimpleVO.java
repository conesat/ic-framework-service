package cn.icframework.hotel.module.roomorder.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomOrderSimpleVO {
    private Long id;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户电话
     */
    private String customerPhone;
    /**
     * 入住时间
     */
    private LocalDate inDate;
    /**
     * 离店时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate outDate;
    /**
     * 房间号
     */
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
     * 入住天数
     */
    private Integer day;
    /**
     * 剩余
     */
    private Integer remainingDay;
    /**
     * 是否已入住
     */
    private Boolean checkIn;
    /**
     * 实付价格
     */
    private Double actualRoomPrice;
}
