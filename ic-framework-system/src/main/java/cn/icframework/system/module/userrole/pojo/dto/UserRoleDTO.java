package cn.icframework.system.module.userrole.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


/**
 * @author ic generator
 * @date 2023/08/09
 */
@Getter
@Setter
public class UserRoleDTO {
    @NotNull
    private String id;
    /**
    * 角色id
    */
    @NotNull
    private Long roleId;
    /**
     * 管理员id
     */
    @NotNull
    private Long userId;
}
