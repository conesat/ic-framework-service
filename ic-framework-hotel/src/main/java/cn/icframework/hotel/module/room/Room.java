package cn.icframework.hotel.module.room;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.common.enums.RoomStatusEnum;
import cn.icframework.hotel.common.enums.SaleStatusEnum;
import cn.icframework.hotel.module.floor.Floor;
import cn.icframework.hotel.module.roomtype.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_room", comment = "房间")
public class Room {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 优惠价
    */
    @TableField(value = "preferential_price", comment = "优惠价")
    private Double preferentialPrice;

    /**
    * 门市价
    */
    @TableField(value = "price", comment = "价格")
    private Double price;

    /**
    * 周末价
    */
    @TableField(value = "week_price", comment = "周末价")
    private Double weekPrice;

    /**
    * 房间号
    */
    @TableField(value = "no", comment = "房间号", length = 20)
    private String no;

    /**
    * 是否在售
    */
    @TableField(value = "sale_status", comment = "是否在售")
    private SaleStatusEnum saleStatus;

    /**
    * 房间状态
    */
    @TableField(value = "room_status", comment = "房间状态")
    private RoomStatusEnum roomStatus;

    /**
     * 所属楼层
     */
    @ForeignKey(references = Floor.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "floor_id", comment = "所属楼层")
    private Long floorId;

    /**
     * 所属房型
     */
    @ForeignKey(references = RoomType.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "room_type_id", comment = "所属房型")
    private Long roomTypeId;

    /**
     * 扩展key
     */
    @TableField(value = "ext_key", comment = "扩展key")
    private String extKey;

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
    public static Room def() {
        Room def = new Room();
        return def;
    }
}
