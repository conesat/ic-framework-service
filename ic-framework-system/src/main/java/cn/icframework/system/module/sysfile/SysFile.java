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
    private Long id;

    /**
     * 文件大小
     */
    @TableField(notNull = true, length = 11, comment = "文件大小", defaultValue = "0")
    private long size;

    /**
     * 文件路径
     */
    @TableField(notNull = true, comment = "ossObjectName")
    private String ossObjectName;

    /**
     * 文件bucketName
     */
    @TableField(notNull = true, comment = "bucketName")
    private String bucketName;

    /**
     * 文件名称
     */
    @TableField(notNull = true, comment = "文件名称")
    private String name;

    /**
     * 文件bucketUrl
     */
    @TableField(notNull = true, comment = "bucketUrl")
    private String bucketUrl;

    /**
     * 文件引用次数
     */
    @TableField(notNull = true, comment = "引用次数", defaultValue = "0")
    private int refCount;

    /**
     * 文件系统类型
     */
    @TableField(notNull = true, comment = "文件系统类型")
    private FileType type;

    /**
     * 文件所属用户
     */
    @TableField(comment = "所属用户")
    private Long userId;

    /**
     * 文件创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
