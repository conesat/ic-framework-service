package cn.icframework.system.module.rolemenu;

import cn.icframework.mybatis.annotation.*;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.menu.Menu;
import cn.icframework.system.module.role.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Index(name = "role_menu", columns = {"role_id", "menu_id"}, unique = true)
@Table(value = "sys_role_menu", comment = "角色菜单")
public class RoleMenu {
    @Id(idType = IdType.AUTO)
    private Long id;

    /**
     * 菜单id
     */
    @ForeignKey(references = Menu.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "menu_id", comment = "菜单id")
    private Long menuId;

    /**
     * 角色id
     */
    @ForeignKey(references = Role.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "role_id", comment = "角色id")
    private Long roleId;

    public static RoleMenu def() {
        return new RoleMenu();
    }
}
