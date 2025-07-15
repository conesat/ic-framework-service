package cn.icframework.system.module.permission.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author create by ic gen
 * @date 2023/06/20
 */
@Getter
@Setter
public class PermissionVO implements Serializable {
    private Long id;
    private String path;
    private Long groupId;
    private String userType;
    private String name;
}
