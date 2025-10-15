package cn.icframework.system.module.rp.service;


import cn.icframework.auth.entity.RP;
import cn.icframework.auth.standard.IUserRPService;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.permission.def.PermissionDef;
import cn.icframework.system.module.permissiongroup.def.PermissionGroupDef;
import cn.icframework.system.module.role.Role;
import cn.icframework.system.module.role.def.RoleDef;
import cn.icframework.system.module.role.service.RoleService;
import cn.icframework.system.module.rp.def.RolePermissionDef;
import cn.icframework.system.module.userrole.def.UserRoleDef;
import cn.icframework.system.module.userrole.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static cn.icframework.mybatis.wrapper.FunctionWrapper.CONCAT;
import static cn.icframework.mybatis.wrapper.Wrapper.SELECT;


/**
 * 用来获取登录用户的角色权限
 *
 * @author IceFire
 * @since 2023/8/9
 */
@Service
@AllArgsConstructor
public class UserRoleProviderService implements IUserRPService {
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;

    @Override
    public RP load(Object userId, String userType) {
        RP rp = new RP();

        RoleDef roleDef = RoleDef.table();
        PermissionDef permissionDef = PermissionDef.table();
        UserRoleDef userRoleDef = UserRoleDef.table();
        PermissionGroupDef permissionGroupDef = PermissionGroupDef.table();
        RolePermissionDef rolePermissionDef = RolePermissionDef.table();

        SqlWrapper selectRoleIdsWrapper = SELECT(roleDef)
                .FROM(userRoleDef)
                .LEFT_JOIN(roleDef).ON(userRoleDef.roleId.eq(roleDef.id))
                .WHERE(userRoleDef.userId.eq(userId));
        List<Role> roles = roleService.select(selectRoleIdsWrapper);
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (!roles.isEmpty()) {
            boolean su = false;
            List<Long> roleIds = new ArrayList<>();
            rp.setRoles(new HashSet<>());
            for (Role role : roles) {
                rp.getRoles().add(role.getSign());
                roleIds.add(role.getId());
                if (role.isSu() && role.isSystem()) {
                    su = true;
                }
            }
            SqlWrapper selectPermissionPathWrapper;
            if (su) {
                selectPermissionPathWrapper = SELECT(CONCAT(permissionGroupDef.path, permissionDef.path))
                        .FROM(permissionDef)
                        .LEFT_JOIN(permissionGroupDef).ON(permissionGroupDef.id.eq(permissionDef.groupId))
                        .WHERE(permissionDef.userType.eq(userType));
            } else {
                selectPermissionPathWrapper = SELECT(CONCAT(permissionGroupDef.path, permissionDef.path))
                        .FROM(rolePermissionDef)
                        .LEFT_JOIN(permissionDef).ON(rolePermissionDef.permissionId.eq(permissionDef.id))
                        .LEFT_JOIN(permissionGroupDef).ON(permissionGroupDef.id.eq(permissionDef.groupId))
                        .WHERE(rolePermissionDef.roleId.in(roleIds), permissionDef.userType.eq(userType));
            }
            List<String> permissionPaths = rolePermissionService.select(selectPermissionPathWrapper, String.class);
            rp.setPermissionPaths(new HashSet<>(permissionPaths));
        }
        return rp;
    }

}
