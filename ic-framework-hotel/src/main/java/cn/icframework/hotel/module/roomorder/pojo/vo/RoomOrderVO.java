package cn.icframework.hotel.module.roomorder.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomOrderVO {
    private Long id;
    /**
     * 客户-用户id
     */
    private Long userId;

    private String userName;

    private Boolean vip;

    private String avatarFileUrl;

    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户电话
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate inDate;
    /**
     * 离店时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate outDate;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 房间号
     */
    private String roomNo;
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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /**
     * 是否已入住
     */
    private Boolean checkIn;
}
