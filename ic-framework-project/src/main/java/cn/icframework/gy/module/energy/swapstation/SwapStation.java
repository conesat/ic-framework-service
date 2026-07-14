package cn.icframework.project.module.energy.swapstation;

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
@Table(value = "swap_station", comment = "换电站")
public class SwapStation {

    /**
     * 主键
     */
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 站点编号
     */
    @TableField(value = "station_code", comment = "站点编号", notNull = true, length = 32)
    private String stationCode;

    /**
     * 站点名称
     */
    @TableField(value = "name", comment = "站点名称", notNull = true, length = 80)
    private String name;

    /**
     * 城市
     */
    @TableField(value = "city", comment = "城市", notNull = true, length = 40)
    private String city;

    /**
     * 区域
     */
    @TableField(value = "district", comment = "区域", length = 40)
    private String district;

    /**
     * 详细地址
     */
    @TableField(value = "address", comment = "详细地址", notNull = true, length = 255)
    private String address;

    /**
     * 经度
     */
    @TableField(value = "longitude", comment = "经度")
    private Double longitude;

    /**
     * 纬度
     */
    @TableField(value = "latitude", comment = "纬度")
    private Double latitude;

    /**
     * 营业时间
     */
    @TableField(value = "business_hours", comment = "营业时间", length = 50)
    private String businessHours;

    /**
     * 联系电话
     */
    @TableField(value = "contact_phone", comment = "联系电话", length = 30)
    private String contactPhone;

    /**
     * 营业状态（1营业中、0停用、2维护）
     */
    @TableField(value = "status", comment = "营业状态（1营业中、0停用、2维护）", notNull = true, defaultValue = "1")
    private Integer status;

    /**
     * 电柜总格口数
     */
    @TableField(value = "total_cabinets", comment = "电柜总格口数", notNull = true, defaultValue = "0")
    private Integer totalCabinets;

    /**
     * 可用电池数
     */
    @TableField(value = "available_battery_count", comment = "可用电池数", notNull = true, defaultValue = "0")
    private Integer availableBatteryCount;

    /**
     * 可还空位数
     */
    @TableField(value = "available_return_slots", comment = "可还空位数", notNull = true, defaultValue = "0")
    private Integer availableReturnSlots;

    /**
     * 评分
     */
    @TableField(value = "rating", comment = "评分", defaultValue = "5.0")
    private Double rating;

    /**
     * 服务设施
     */
    @TableField(value = "facilities", comment = "服务设施", length = 2000)
    private String facilities;

    /**
     * 站点公告
     */
    @TableField(value = "announcement", comment = "站点公告", length = 2000)
    private String announcement;

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
    public static SwapStation def() {
        SwapStation def = new SwapStation();
        return def;
    }
}
