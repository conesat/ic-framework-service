package cn.icframework.system.module.permission;

import cn.icframework.auth.entity.BasicPermission;
import cn.icframework.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;


/**
 * @author hzl
 * @since 2023/6/20
 */
@Getter
@Setter
@Table(value = "sys_permission", comment = "权限")
public class Permission extends BasicPermission {
}
