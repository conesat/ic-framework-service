package cn.icframework.system.module.rp;

import cn.icframework.mybatis.annotation.*;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.permission.Permission;
import cn.icframework.system.module.role.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author IceFire
 * @since 2023/8/7
 */
@Getter
@Setter
@Index(name = "role_permission_idx", columns = {"role_id", "permission_id"}, unique = true)
@Table(value = "sys_role_permission", comment = "角色权限")
public class RolePermission {
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 角色id
     */
    @ForeignKey(references = Role.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "角色id")
    private Long roleId;

    /**
     * 权限id
     */
    @ForeignKey(references = Permission.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "权限id")
    private Long permissionId;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "更新时间", onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateTime;
}
