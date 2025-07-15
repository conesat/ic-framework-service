package cn.icframework.hotel.module.roomsupporting;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.module.room.Room;
import cn.icframework.hotel.module.supporting.Supporting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(value = "hotel_room_supporting", comment = "房间配套")
public class RoomSupporting {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 房间id
    */
    @ForeignKey(references = Room.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "room_id", comment = "房间id")
    private Long roomId;

    /**
    * 配套id
    */
    @ForeignKey(references = Supporting.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "supporting_id", comment = "配套id")
    private Long supportingId;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static RoomSupporting def() {
        RoomSupporting def = new RoomSupporting();
        return def;
    }
}
