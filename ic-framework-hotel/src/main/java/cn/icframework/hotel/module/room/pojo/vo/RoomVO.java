package cn.icframework.hotel.module.room.pojo.vo;

import cn.icframework.hotel.common.enums.RoomStatusEnum;
import cn.icframework.hotel.common.enums.SaleStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class RoomVO {
    private Long id;
    /**
     * 优惠价
     */
    private Double preferentialPrice;

    /**
     * 门市价
     */
    private Double price;
    /**
     * 周末价
     */
    private Double weekPrice;
    /**
     * 房间号
     */
    private String no;
    /**
     * 是否在售
     */
    private SaleStatusEnum saleStatus;
    /**
     * 房间状态
     */
    private RoomStatusEnum roomStatus;
    /**
     * 所属楼层
     */
    private Long floorId;
    private String floorName;
    /**
     * 所属楼栋
     */
    private Long buildingId;
    private String buildingName;
    /**
     * 所属酒店
     */
    private Long hotelId;
    private String hotelName;
    /**
     * 所属房型
     */
    private Long roomTypeId;
    private String roomTypeName;
    /**
     * 扩展key
     */
    private String extKey;

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

}
