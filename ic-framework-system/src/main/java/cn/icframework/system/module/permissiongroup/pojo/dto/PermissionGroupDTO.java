package cn.icframework.system.module.permissiongroup.pojo.dto;

import java.lang.Long;
import java.lang.String;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/08/02
 */
@Getter
@Setter
public class PermissionGroupDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String path;

}
