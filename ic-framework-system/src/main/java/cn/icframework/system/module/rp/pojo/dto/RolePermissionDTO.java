package cn.icframework.system.module.rp.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic generator
 * @date 2023/08/07
 */
@Getter
@Setter
public class RolePermissionDTO {
    private Long id;
    /**
    * 角色id
    */
    @NotNull
    private Long roleId;
    /**
    * 权限id
    */
    @NotNull
    private String permissionSign;

}
