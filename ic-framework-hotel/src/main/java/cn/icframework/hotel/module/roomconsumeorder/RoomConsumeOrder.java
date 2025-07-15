package cn.icframework.hotel.module.roomconsumeorder;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.common.enums.RoomConsumeOrderStatus;
import cn.icframework.hotel.module.consumeitem.ConsumeItem;
import cn.icframework.hotel.module.roomorder.RoomOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_room_consume_order", comment = "额外消费")
public class RoomConsumeOrder {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 所属订单
     */
    @ForeignKey(references = RoomOrder.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "room_order_id", comment = "所属订单")
    private Long roomOrderId;

    /**
     * 项目id
     */
    @ForeignKey(references = ConsumeItem.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "customer_item_id", comment = "项目id")
    private Long customerItemId;

    /**
     * 金额
     */
    @TableField(value = "price", comment = "金额")
    private Double price;

    /**
     * 数量
     */
    @TableField(value = "num", comment = "数量")
    private Integer num;

    /**
     * 商品名称
     */
    @TableField(value = "customer_item_name", comment = "商品名称")
    private String customerItemName;

    /**
     * 状态
     */
    @TableField(value = "status", comment = "状态")
    private RoomConsumeOrderStatus status;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time", comment = "支付时间")
    private LocalDateTime payTime;

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
    public static RoomConsumeOrder def() {
        RoomConsumeOrder def = new RoomConsumeOrder();
        def.setStatus(RoomConsumeOrderStatus.UN_PAY);
        return def;
    }
}
