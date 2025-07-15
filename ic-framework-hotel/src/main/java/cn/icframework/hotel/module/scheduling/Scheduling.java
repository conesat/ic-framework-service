package cn.icframework.hotel.module.scheduling;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Table(value = "hotel_scheduling", comment = "排班")
public class Scheduling {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 所属用户
    */
    @TableField(value = "user_id", comment = "所属用户")
    private Long userId;

    /**
    * 班次
    */
    @TableField(value = "time_scope_id", comment = "班次")
    private Long timeScopeId;

    /**
    * 日期
    */
    @TableField(value = "date", notNull = true, comment = "日期")
    private LocalDate date;

    /**
    * 签到时间
    */
    @TableField(value = "sign_in_time", comment = "签到时间")
    private LocalTime signInTime;

    /**
    * 签退时间
    */
    @TableField(value = "sign_out_time", comment = "签退时间")
    private LocalTime signOutTime;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static Scheduling def() {
        Scheduling def = new Scheduling();
        return def;
    }
}
