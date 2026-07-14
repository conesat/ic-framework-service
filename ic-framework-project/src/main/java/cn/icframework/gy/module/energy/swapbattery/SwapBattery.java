package cn.icframework.project.module.energy.swapbattery;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2026/07/13
 */
@Getter
@Setter
@Table(value = "swap_battery", comment = "换电电池")
public class SwapBattery {

    /**
     * 主键
     */
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 电池编号
     */
    @TableField(value = "battery_no", comment = "电池编号", notNull = true, length = 50)
    private String batteryNo;

    /**
     * 所属换电站
     */
    @TableField(value = "station_id", comment = "所属换电站")
    private Long stationId;

    /**
     * 电池型号
     */
    @TableField(value = "model", comment = "电池型号", length = 50)
    private String model;

    /**
     * 电压（V）
     */
    @TableField(value = "voltage", comment = "电压（V）")
    private Integer voltage;

    /**
     * 容量（Ah）
     */
    @TableField(value = "capacity", comment = "容量（Ah）")
    private Integer capacity;

    /**
     * 当前电量（%）
     */
    @TableField(value = "power_percent", comment = "当前电量（%）", notNull = true, defaultValue = "100")
    private Integer powerPercent;

    /**
     * 健康度（%）
     */
    @TableField(value = "health_percent", comment = "健康度（%）", notNull = true, defaultValue = "100")
    private Integer healthPercent;

    /**
     * 状态（1可租、2使用中、3暂存、4维修、0停用）
     */
    @TableField(value = "status", comment = "状态（1可租、2使用中、3暂存、4维修、0停用）", notNull = true, defaultValue = "1")
    private Integer status;

    /**
     * 最近换电时间
     */
    @TableField(value = "last_swap_time", comment = "最近换电时间")
    private LocalDateTime lastSwapTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", onInsertValue = "now()", comment = "创建时间", notNull = true)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", onUpdateValue = "now()", comment = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 一般创建对象通过这个方法
     * 可以统一为对象赋初始值
     */
    public static SwapBattery def() {
        SwapBattery def = new SwapBattery();
        return def;
    }
}
