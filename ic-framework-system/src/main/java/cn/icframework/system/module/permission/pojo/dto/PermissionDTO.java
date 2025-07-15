package cn.icframework.system.module.permission.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author create by ic gen
 * @date 2023/06/20
 */
@Getter
@Setter
public class PermissionDTO {
    private Long id;
    private String path;
    private Long groupId;
    private String userType;
    private String name;
}
