package cn.icframework.project.module.energy.repairorder;

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
@Table(value = "repair_order", comment = "维修工单")
public class RepairOrder {

    /**
     * 主键
     */
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 工单编号
     */
    @TableField(value = "order_no", comment = "工单编号", notNull = true, length = 50)
    private String orderNo;

    /**
     * 用户ID
     */
    @TableField(value = "user_id", comment = "用户ID", notNull = true)
    private Long userId;

    /**
     * 关联站点ID
     */
    @TableField(value = "station_id", comment = "关联站点ID")
    private Long stationId;

    /**
     * 故障类型
     */
    @TableField(value = "repair_type", comment = "故障类型", notNull = true, length = 50)
    private String repairType;

    /**
     * 问题描述
     */
    @TableField(value = "description", comment = "问题描述", length = 2000)
    private String description;

    /**
     * 图片地址
     */
    @TableField(value = "images", comment = "图片地址", length = 2000)
    private String images;

    /**
     * 联系电话
     */
    @TableField(value = "contact_phone", comment = "联系电话", length = 30)
    private String contactPhone;

    /**
     * 状态（1待处理、2处理中、3已完成、4已取消）
     */
    @TableField(value = "status", comment = "状态（1待处理、2处理中、3已完成、4已取消）", notNull = true, defaultValue = "1")
    private Integer status;

    /**
     * 处理人ID
     */
    @TableField(value = "handler_id", comment = "处理人ID")
    private Long handlerId;

    /**
     * 处理说明
     */
    @TableField(value = "handle_remark", comment = "处理说明", length = 2000)
    private String handleRemark;

    /**
     * 处理时间
     */
    @TableField(value = "repair_time", comment = "处理时间")
    private LocalDateTime repairTime;

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
    public static RepairOrder def() {
        RepairOrder def = new RepairOrder();
        return def;
    }
}
