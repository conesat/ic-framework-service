package cn.icframework.hotel.module.building;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.module.hotel.Hotel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_building", comment = "楼栋")
public class Building {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 名称
    */
    @TableField(value = "name", comment = "名称")
    private String name;

    /**
    * 所属酒店
    */
    @ForeignKey(references = Hotel.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "hotel_id", comment = "所属酒店")
    private Long hotelId;

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
    public static Building def() {
        Building def = new Building();
        return def;
    }
}
