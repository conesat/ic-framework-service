package cn.icframework.system.module.rp.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.permission.def.PermissionDef;
import cn.icframework.system.module.permission.service.PermissionService;
import cn.icframework.system.module.role.Role;
import cn.icframework.system.module.role.service.RoleService;
import cn.icframework.system.module.rp.RolePermission;
import cn.icframework.system.module.rp.dao.RolePermissionMapper;
import cn.icframework.system.module.rp.def.RolePermissionDef;
import cn.icframework.system.module.rp.pojo.dto.RolePermissionDTO;
import cn.icframework.system.module.rp.wrapperbuilder.RolePermissionWrapperBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * @author ic generator
 * @since 2023/08/07
 */
@Service
@AllArgsConstructor
public class RolePermissionService extends BasicService<RolePermissionMapper, RolePermission> {
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final RolePermissionWrapperBuilder wrapperBuilder;


    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(RolePermissionDTO dto) {
        RolePermission entity = dto.getId() != null ? selectById(dto.getId()) : new RolePermission();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() == null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }


    @Transactional
    public void editBatch(Long roleId, Long[] permissionIds, Boolean status) {
        Role role = roleService.selectById(roleId);
        Assert.isNotNull(role, "编辑失败，角色已被删除");
        Assert.isFalse(role.isSystem(), "系统角色不允许编辑");
        RolePermissionDef table = RolePermissionDef.table();
        if (!status) {
            delete(table.roleId.eq(roleId).permissionId.in(List.of(permissionIds)));
            return;
        }
        List<Long> pids = select(SELECT(table.permissionId).FROM(table).WHERE(table.roleId.eq(roleId).permissionId.in(List.of(permissionIds))), Long.class);
        HashSet<Long> pidsSet = new HashSet<>(pids);
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            if (pidsSet.contains(permissionId)) {
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        }
        if (!rolePermissions.isEmpty()) {
            insertBatch(rolePermissions);
        }
    }

    /**
     * 获取角色全部权限
     *
     * @param roleId
     * @return
     */
    public List<Long> allPermissionIdsByRoleId(Long roleId) {
        Role role = roleService.selectById(roleId);
        Assert.isNotNull(role, "角色不存在");
        PermissionDef permissionDef = PermissionDef.table();

        if (role.isSu()) {
            return permissionService.select(SELECT(permissionDef.id).FROM(permissionDef).WHERE(permissionDef.userType.eq(role.getUserType())), Long.class);
        }
        return select(wrapperBuilder.allPermissionIdsByRoleId(roleId), Long.class);
    }
}
