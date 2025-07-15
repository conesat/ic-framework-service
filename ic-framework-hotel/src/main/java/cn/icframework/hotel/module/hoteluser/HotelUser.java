package cn.icframework.hotel.module.hoteluser;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_user", comment = "酒店用户")
public class HotelUser {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name", notNull = true, comment = "名称")
    private String name;

    /**
     * 手机号
     */
    @TableField(value = "phone", notNull = true, comment = "手机号")
    private String phone;

    @TableField(value = "avatar_file_id", comment = "头像文件", length = 255)
    private Long avatarFileId;

    @TableField(value = "avatar_file_url", comment = "头像文件地址", length = 255)
    private String avatarFileUrl;

    /**
     * 是否vip
     */
    @TableField(value = "vip", defaultValue = "false", comment = "是否vip")
    private Boolean vip;

    /**
     * 是否有效
     */
    @TableField(value = "status", notNull = true, comment = "是否有效")
    private Status status;

    /**
     * vip开始时间
     */
    @TableField(value = "vip_start_time", comment = "vip开始时间")
    private LocalDateTime vipStartTime;

    /**
     * vip到期时间
     */
    @TableField(value = "vip_end_time", comment = "vip到期时间")
    private LocalDateTime vipEndTime;

    /**
     * 累计消费金额
     */
    @TableField(value = "total_consume", comment = "累计消费金额")
    private BigDecimal totalConsume;

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
    public static HotelUser def() {
        HotelUser def = new HotelUser();
        return def;
    }
}
