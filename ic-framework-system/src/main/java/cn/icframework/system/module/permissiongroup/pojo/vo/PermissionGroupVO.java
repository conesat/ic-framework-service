package cn.icframework.system.module.permissiongroup.pojo.vo;

import cn.icframework.mybatis.annotation.Collection;
import cn.icframework.system.module.permission.pojo.vo.PermissionVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author ic
 * @date 2024/08/02
 */
@Getter
@Setter
public class PermissionGroupVO implements Serializable {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 权限列表
     */
    @Collection(prefix = "permissions", groupMainId = "id")
    private List<PermissionVO> permissionVOS;
}
