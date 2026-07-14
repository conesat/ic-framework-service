package cn.icframework.project.module.energy.swaporder;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2026/07/13
 */
@Getter
@Setter
@Table(value = "swap_order", comment = "换电订单")
public class SwapOrder {

    /**
     * 主键
     */
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 订单号
     */
    @TableField(value = "order_no", comment = "订单号", notNull = true, length = 50)
    private String orderNo;

    /**
     * 用户ID
     */
    @TableField(value = "user_id", comment = "用户ID", notNull = true)
    private Long userId;

    /**
     * 换电站ID
     */
    @TableField(value = "station_id", comment = "换电站ID")
    private Long stationId;

    /**
     * 电池ID
     */
    @TableField(value = "battery_id", comment = "电池ID")
    private Long batteryId;

    /**
     * 订单类型（1换电、2暂存、3租赁）
     */
    @TableField(value = "order_type", comment = "订单类型（1换电、2暂存、3租赁）", notNull = true, defaultValue = "1")
    private Integer orderType;

    /**
     * 订单状态（1进行中、2完成、3异常、4取消）
     */
    @TableField(value = "status", comment = "订单状态（1进行中、2完成、3异常、4取消）", notNull = true, defaultValue = "1")
    private Integer status;

    /**
     * 实付金额
     */
    @TableField(value = "amount", comment = "实付金额", length = 10, fraction = 2, defaultValue = "0")
    private BigDecimal amount;

    /**
     * 开始时间
     */
    @TableField(value = "start_time", comment = "开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time", comment = "结束时间")
    private LocalDateTime endTime;

    /**
     * 备注
     */
    @TableField(value = "remark", comment = "备注", length = 255)
    private String remark;

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
    public static SwapOrder def() {
        SwapOrder def = new SwapOrder();
        return def;
    }
}
