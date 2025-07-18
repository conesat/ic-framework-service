package cn.icframework.system.module.dep;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.common.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author hzl
 * @since 2023/6/21
 */
@Getter
@Setter
@Table(value = "sys_dept", comment = "部门")
public class Dept {
    public final static String PATH_CHAR = "_"; // path分隔符

    @Id(idType = IdType.AUTO)
    private Long id;

    /**
     * 部门名称
     */
    @NotEmpty
    @TableField(notNull = true, comment = "部门名称")
    private String name;

    /**
     * 部门完整id路径
     */
    @TableField(comment = "上级完整id路径")
    private String parentPath;

    /**
     * 上级id
     */
    @TableField(comment = "上级id")
    private Long parentId;

    /**
     * 部门排序
     */
    @TableField(length = 4, comment = "排序", defaultValue = "0")
    private int sort;

    /**
     * 部门电话
     */
    @TableField(length = 20, comment = "部门电话")
    private String phone;

    /**
     * 部门状态
     */
    @TableField(notNull = true, defaultValue = "1", comment = "是否有效")
    private Status status;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
