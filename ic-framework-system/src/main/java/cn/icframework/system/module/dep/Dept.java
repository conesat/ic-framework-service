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
 * @date 2023/6/21
 */
@Getter
@Setter
@Table(value = "sys_dept", comment = "部门")
public class Dept {
    public final static String PATH_CHAR = "_"; // path分隔符

    @Id(idType = IdType.AUTO)
    @TableField
    private Long id;

    @NotEmpty
    @TableField(value = "name", notNull = true, comment = "部门名称")
    private String name;

    @TableField(value = "parent_path", comment = "上级完整id路径")
    private String parentPath;

    @TableField(value = "parent_id", comment = "上级id")
    private Long parentId;

    @TableField(value = "sort", length = 4, comment = "排序", defaultValue = "0")
    private int sort;

    @TableField(value = "phone", length = 20, comment = "部门电话")
    private String phone;

    @TableField(value = "status", notNull = true, defaultValue = "1", comment = "是否有效")
    private Status status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;


    public static Dept def() {
        return new Dept();
    }
}
