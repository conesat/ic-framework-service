package cn.icframework.hotel.module.timescope;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Table(value = "hotel_time_scope", comment = "时间表")
public class TimeScope {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 名称
    */
    @TableField(value = "name", comment = "名称")
    private String name;

    /**
    * 开始时间
    */
    @TableField(value = "start_time", notNull = true, comment = "开始时间")
    private LocalTime startTime;

    /**
    * 所属部门
    */
    @TableField(value = "dept_id", comment = "所属部门")
    private Long deptId;

    /**
    * 结束时间
    */
    @TableField(value = "end_time", notNull = true, comment = "结束时间")
    private LocalTime endTime;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static TimeScope def() {
        TimeScope def = new TimeScope();
        return def;
    }
}
