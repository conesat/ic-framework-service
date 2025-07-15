package cn.icframework.system.module.sysfile;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.enums.FileType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "sys_file", comment = "系统文件")
public class SysFile {
    @Id(idType = IdType.SNOWFLAKE)
    @TableField
    private Long id;

    @TableField(value = "size", notNull = true, length = 11, comment = "文件大小", defaultValue = "0")
    private long size;

    @TableField(value = "oss_object_name", notNull = true, comment = "ossObjectName")
    private String ossObjectName;

    @TableField(value = "bucket_name", notNull = true, comment = "bucketName")
    private String bucketName;

    @TableField(value = "name", notNull = true, comment = "文件名称")
    private String name;

    @TableField(value = "bucket_url", notNull = true, comment = "bucketUrl")
    private String bucketUrl;

    @TableField(value = "ref_count", notNull = true, comment = "引用次数", defaultValue = "0")
    private int refCount;

    @TableField(value = "type", notNull = true, comment = "文件系统类型")
    private FileType type;

    @TableField(value = "user_id", comment = "所属用户")
    private Long userId;


    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    public static SysFile def() {
        return new SysFile();
    }
}
