package cn.icframework.system.module.userrole;

import cn.icframework.mybatis.annotation.*;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.role.Role;
import cn.icframework.system.module.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author hzl
 * @since 2023/8/9
 */
@Getter
@Setter
@Index(name = "user_role_idx", columns = {"user_id", "role_id"}, unique = true)
@Table(value = "sys_user_role", comment = "用户角色关联")
public class UserRole {
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    @ForeignKey(references = Role.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "角色id")
    private Long roleId;

    @ForeignKey(references = User.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "用户id")
    private Long userId;

    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
