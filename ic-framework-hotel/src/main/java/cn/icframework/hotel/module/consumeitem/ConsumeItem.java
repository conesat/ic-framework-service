package cn.icframework.hotel.module.consumeitem;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.sysfile.SysFile;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_consume_item", comment = "消费项目")
public class ConsumeItem {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 名称
    */
    @TableField(value = "name", comment = "名称")
    private String name;

    /**
    * 价格
    */
    @TableField(value = "price", comment = "价格")
    private Double price;

    /**
    * 库存
    */
    @TableField(value = "inventory", comment = "库存")
    private Integer inventory;

    /**
     * 文件id
     */
    @ForeignKey(references = SysFile.class, onDelete = ForeignKeyAction.SET_NULL)
    @TableField(value = "file_id", comment = "文件id")
    private Long fileId;

    /**
     * 文件url
     */
    @TableField(value = "file_url", comment = "文件url")
    private String fileUrl;

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
    public static ConsumeItem def() {
        ConsumeItem def = new ConsumeItem();
        return def;
    }
}
