package cn.icframework.system.module.rp.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ic generator
 * @date 2023/08/07
 */
@Getter
@Setter
public class RolePermissionVO {
    private Long id;
    /**
    * 角色id
    */
    private Long roleId;
    /**
    * 权限id
    */
    private String permissionSign;

}
