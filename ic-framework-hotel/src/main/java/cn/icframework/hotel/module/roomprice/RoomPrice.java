package cn.icframework.hotel.module.roomprice;

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
@Table(value = "hotel_room_price", comment = "房价")
public class RoomPrice {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 门市价
    */
    @TableField(value = "price", comment = "门市价")
    private Double price;

    /**
    * 日期
    */
    @TableField(value = "date", comment = "日期")
    private LocalDate date;


    /**
    * 所属房间
    */
    @ForeignKey(references = Room.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "room_id", comment = "所属房间")
    private Long roomId;


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
    public static RoomPrice def() {
        RoomPrice def = new RoomPrice();
        return def;
    }
}
