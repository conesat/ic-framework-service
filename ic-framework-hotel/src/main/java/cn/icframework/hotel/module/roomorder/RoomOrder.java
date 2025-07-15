package cn.icframework.hotel.module.roomorder;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.module.room.Room;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_room_order", comment = "房间订单")
public class RoomOrder {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 客户-用户id
    */
    @TableField(value = "user_id", comment = "客户-用户id")
    private Long userId;

    /**
    * 客户姓名
    */
    @TableField(value = "customer_name", comment = "客户姓名")
    private String customerName;

    /**
    * 客户电话
    */
    @TableField(value = "customer_phone", comment = "客户电话")
    private String customerPhone;

    /**
    * 房间价格
    */
    @TableField(value = "room_price", comment = "房间价格")
    private Double roomPrice;

    /**
    * 实付价格
    */
    @TableField(value = "actual_room_price", comment = "实付价格")
    private Double actualRoomPrice;

    /**
    * 入住时间
    */
    @TableField(value = "in_date", comment = "入住时间")
    private LocalDate inDate;

    /**
    * 离店时间
    */
    @TableField(value = "out_date", comment = "离店时间")
    private LocalDate outDate;

    /**
    * 房间号
    */
    @ForeignKey(references = Room.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "room_id", comment = "房间号")
    private Long roomId;

    /**
    * 备注
    */
    @TableField(value = "remark", length = 500, comment = "备注")
    private String remark;

    /**
    * 是否已支付
    */
    @TableField(value = "payed", comment = "是否已支付")
    private Boolean payed;

    /**
    * 入住天数
    */
    @TableField(value = "day", comment = "入住天数")
    private Integer day;

    /**
     * 是否已入住
     */
    @TableField(value = "check_in", comment = "是否已入住")
    private Boolean checkIn;

    /**
    * 创建时间
    */
    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;


    /**
    * 更新时间
    */
    @TableField(value = "update_time", comment = "更新时间", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static RoomOrder def() {
        RoomOrder def = new RoomOrder();
        def.setCheckIn(false);
        return def;
    }
}
