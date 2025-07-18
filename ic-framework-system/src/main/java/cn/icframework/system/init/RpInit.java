package cn.icframework.system.init;

import cn.icframework.common.enums.Status;
import cn.icframework.common.utils.ConvertUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.InitMd5Keys;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.init.helper.InitHelper;
import cn.icframework.system.module.permission.def.PermissionDef;
import cn.icframework.system.module.permission.service.PermissionService;
import cn.icframework.system.module.permissiongroup.def.PermissionGroupDef;
import cn.icframework.system.module.role.Role;
import cn.icframework.system.module.role.def.RoleDef;
import cn.icframework.system.module.role.service.RoleService;
import cn.icframework.system.module.rp.RolePermission;
import cn.icframework.system.module.rp.def.RolePermissionDef;
import cn.icframework.system.module.rp.service.RolePermissionService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.icframework.mybatis.wrapper.Wrapper.SELECT;

/**
 * @author hzl
 * @since 2024/9/4
 */
@Component
@RequiredArgsConstructor
public class RpInit {

    private final InitHelper initHelper;
    private final PermissionService permissionService;

    private final RoleService roleService;

    private final RolePermissionService rpService;

    public void initRp() {
        List<Role> newRoles = initRoles();
        initRPs(newRoles);
    }


    /**
     * 只处理新增护具
     *
     * @param newRoles
     * @throws IOException
     */
    private void initRPs(List<Role> newRoles) {
        if (CollectionUtils.isEmpty(newRoles)) {
            return;
        }
        Map<String, Role> roleMap = newRoles.stream().collect(Collectors.toMap(Role::getSign, r -> r));
        RolePermissionDef rolePermissionDef = RolePermissionDef.table();
        // 移除角色所有旧权限
        rpService.delete(rolePermissionDef.roleId.in(newRoles.stream().map(Role::getId).collect(Collectors.toList())));

        initHelper.processFile("/init/rp/rolePermissions.json", null, content -> {
            JSONArray rolesArr = JSONArray.parseArray(content);
            PermissionDef permissionDef = PermissionDef.table();
            PermissionGroupDef permissionGroupDef = PermissionGroupDef.table();
            for (int i = 0; i < rolesArr.size(); i++) {
                JSONObject object = rolesArr.getJSONObject(i);
                String sign = object.getString("sign");
                if (!roleMap.containsKey(sign)) {
                    continue;
                }
                Role role = roleMap.get(sign);

                Boolean all = object.getBoolean("all");
                if (all != null && all) {
                    List<Long> allPermissionIds = permissionService.select(SELECT(permissionDef.id).FROM(permissionDef.userType.eq(UserType.SYSTEM_USER)), Long.class);
                    insertRP(allPermissionIds, role);
                    continue;
                }

                JSONArray permissions = object.getJSONArray("permissions");
                for (int i1 = 0; i1 < permissions.size(); i1++) {
                    JSONObject permissionData = permissions.getJSONObject(i1);
                    String groupPath = permissionData.getString("groupPath");
                    JSONArray paths = permissionData.getJSONArray("paths");

                    SqlWrapper sqlWrapper = SELECT(permissionDef.id)
                            .FROM(permissionDef, permissionGroupDef)
                            .WHERE(permissionDef.groupId.eq(permissionGroupDef.id).path.in(paths), permissionGroupDef.path.eq(groupPath));

                    List<Long> permissionIds = permissionService.select(sqlWrapper, Long.class);
                    insertRP(permissionIds, role);
                }
            }
        });
    }

    private void insertRP(List<Long> allPermissionIds, Role role) {
        List<RolePermission> insertRolePermissions = new ArrayList<>();
        for (Long permissionId : allPermissionIds) {
            RolePermission rp = new RolePermission();
            rp.setPermissionId(permissionId);
            rp.setRoleId(role.getId());
            insertRolePermissions.add(rp);
        }
        rpService.insertBatch(insertRolePermissions, 100);
    }


    private List<Role> initRoles() {
        List<Role> signs = new ArrayList<>();
        initHelper.processFile("/init/rp/roles.json", InitMd5Keys.ROLE_INIT_MD5, content -> {
            RoleDef roleDef = RoleDef.table();
            JSONArray rolesArr = JSONArray.parseArray(content);
            for (int i = 0; i < rolesArr.size(); i++) {
                JSONObject object = rolesArr.getJSONObject(i);
                Role role = roleService.selectOne(roleDef.sign.eq(object.getString("sign")));
                String name = object.getString("name");
                String userType = object.getString("userType");
                if (role == null) {
                    role = new Role();
                    role.setSystem(ConvertUtils.toBoolean(object.getString("system"), false));
                    role.setStatus(Status.ENABLE);
                    role.setSign(object.getString("sign"));
                    role.setName(name);
                    role.setUserType(userType);
                    roleService.insert(role);
                    signs.add(role);
                } else if (!role.getName().equals(name) || !role.getUserType().equals(userType)) {
                    role.setName(name);
                    role.setUserType(userType);
                    roleService.updateById(role);
                    signs.add(role);
                }
            }
        });
        return signs;
    }
}
