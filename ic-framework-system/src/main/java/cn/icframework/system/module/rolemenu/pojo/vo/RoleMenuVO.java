package cn.icframework.system.module.rolemenu.pojo.vo;

import java.lang.Long;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2024/08/25
 */
@Getter
@Setter
public class RoleMenuVO {
    private Long id;
    /**
     * 菜单id
     */
    private Long menuId;
    /**
     * 角色id
     */
    private Long roleId;

}
