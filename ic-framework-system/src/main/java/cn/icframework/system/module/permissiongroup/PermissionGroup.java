package cn.icframework.system.module.permissiongroup;

import cn.icframework.auth.entity.BasicPermissionGroup;
import cn.icframework.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hzl
 * @since  2023/6/20 0020
 */
@Getter
@Setter
@Table(value = "sys_permission_group", comment = "权限分组")
public class PermissionGroup extends BasicPermissionGroup {
}
