package cn.icframework.hotel.module.floor;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.module.building.Building;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_floor", comment = "楼层")
public class Floor {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 名称
    */
    @TableField(value = "name", comment = "名称")
    private String name;

    /**
    * 外部key
    */
    @TableField(value = "ext_key", comment = "外部key")
    private String extKey;

    /**
    * 楼层
    */
    @TableField(value = "level", comment = "楼层")
    private Integer level;

    /**
     * 所属楼栋
     */
    @ForeignKey(references = Building.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "building_id", comment = "所属楼栋")
    private Long buildingId;

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
    public static Floor def() {
        Floor def = new Floor();
        return def;
    }
}
