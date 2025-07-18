package cn.icframework.system.module.rolemenu.pojo.dto;

import java.lang.Long;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2024/08/25
 */
@Getter
@Setter
public class RoleMenuDTO {
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
