package cn.icframework.hotel.module.hotel;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_hotel", comment = "酒店")
public class Hotel {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 名称
    */
    @TableField(value = "name", comment = "名称")
    private String name;

    /**
    * 星级
    */
    @TableField(value = "star_level", comment = "星级")
    private Integer starLevel;

    /**
    * 电话
    */
    @TableField(value = "tel", comment = "电话")
    private String tel;

    /**
    * 封面文件id
    */
    @TableField(value = "pic_file_id", comment = "封面文件id")
    private Long picFileId;

    /**
    * 封面
    */
    @TableField(value = "pic_file_url", comment = "封面")
    private String picFileUrl;

    /**
    * 酒店经理
    */
    @TableField(value = "manager_user_id", comment = "酒店经理")
    private String managerUserId;

    /**
    * 地址
    */
    @TableField(value = "address", comment = "地址")
    private String address;

    /**
     * 经度
     */
    @TableField(value = "latitude", comment = "经度")
    private Double latitude;

    /**
     * 纬度
     */
    @TableField(value = "longitude", comment = "纬度")
    private Double longitude;

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
    public static Hotel def() {
        Hotel def = new Hotel();
        return def;
    }
}
