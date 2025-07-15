package cn.icframework.hotel.module.roomtype;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(value = "hotel_room_type", comment = "房型")
public class RoomType {

    @Id(idType = IdType.AUTO)
    private Long id;

    /**
    * 名称
    */
    @TableField(value = "name", comment = "名称")
    private String name;

    /**
    * 标准人数
    */
    @TableField(value = "people_count", comment = "标准人数")
    private Integer peopleCount;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static RoomType def() {
        RoomType def = new RoomType();
        return def;
    }
}
