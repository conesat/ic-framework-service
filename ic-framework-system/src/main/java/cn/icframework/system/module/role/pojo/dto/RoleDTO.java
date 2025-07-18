package cn.icframework.system.module.role.pojo.dto;

import cn.icframework.common.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.Setter;

/**
 * @author create by ic gen
 * @since 2023/06/20
 */
@Getter
@Setter
public class RoleDTO {
    private Long id;
    /**
     * 状态
     */
    @NotNull
    private Status status;
    @NotEmpty
    private String userType;
    /**
     * 名称
     */
    @NotEmpty
    private String name;
    @NotEmpty
    private String sign;
}
