package cn.icframework.system.init;

import cn.icframework.auth.entity.PermissionGroupInit;
import cn.icframework.auth.standard.IPermissionInitService;
import cn.icframework.core.utils.MD5Util;
import cn.icframework.system.consts.InitMd5Keys;
import cn.icframework.system.module.permission.Permission;
import cn.icframework.system.module.permission.def.PermissionDef;
import cn.icframework.system.module.permission.service.PermissionService;
import cn.icframework.system.module.permissiongroup.PermissionGroup;
import cn.icframework.system.module.permissiongroup.service.PermissionGroupService;
import cn.icframework.system.utils.InitMd5Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author IceFire
 * @since 2023/7/22
 */
@Service
@RequiredArgsConstructor
public class PermissionInit implements IPermissionInitService {

    private final PermissionService permissionService;
    private final PermissionGroupService permissionGroupService;

    @Transactional
    @Override
    public void init(List<PermissionGroupInit> permissionGroupInits) {

        String oldMd5 = InitMd5Utils.getMd5String(InitMd5Keys.PERMISSION_INIT_MD5);

        String md5 = MD5Util.encode(permissionGroupInits.toString());
        if (Objects.equals(oldMd5, md5)) {
            // md5一致无需更新
            return;
        }

        // 获取全部原有权限组
        List<PermissionGroup> dbGroups = permissionGroupService.selectAll();

        Map<String, PermissionGroup> dbPermissionGroupMap = new HashMap<>();
        // 记录新数据里面没有的权限组，用于删除
        List<Long> needDeleteGroupIds = new ArrayList<>();
        Set<String> newGroupPath = permissionGroupInits.stream().map(PermissionGroupInit::getPath).collect(Collectors.toSet());
        for (PermissionGroup dbGroup : dbGroups) {
            dbPermissionGroupMap.put(dbGroup.getPath(), dbGroup);
            if (!newGroupPath.contains(dbGroup.getPath())) {
                needDeleteGroupIds.add(dbGroup.getId());
            }
        }

        // 移除数组关联的下级权限
        PermissionDef permissionDef = PermissionDef.table();
        if (!needDeleteGroupIds.isEmpty()) {
            permissionService.delete(permissionDef.groupId.in(needDeleteGroupIds));
        }
        // 移除权限组
        if (!needDeleteGroupIds.isEmpty()) {
            permissionGroupService.deleteByIds(needDeleteGroupIds);
        }

        for (PermissionGroupInit permissionGroupInit : permissionGroupInits) {
            boolean newGroup = false;
            PermissionGroup permissionGroup = dbPermissionGroupMap.get(permissionGroupInit.getPath());
            if (permissionGroup != null && !permissionGroup.getName().equals(permissionGroupInit.getName())) {
                permissionGroup.setName(permissionGroupInit.getName());
                permissionGroupService.updateById(permissionGroup);
            } else if (permissionGroup == null) {
                newGroup = true;
                permissionGroup = new PermissionGroup();
                permissionGroup.setPath(permissionGroupInit.getPath());
                permissionGroup.setName(permissionGroupInit.getName());
                permissionGroupService.insert(permissionGroup);
            }

            Map<String, Permission> dbPermissionMap = new HashMap<>();
            // 并非新增权限
            if (!newGroup) {
                List<String> permissionPaths = permissionGroupInit.getPermissions().stream().map(PermissionGroupInit.Permission::getPath).collect(Collectors.toList());
                // 删除这次被移除的权限
                permissionService.delete(permissionDef.groupId.eq(permissionGroup.getId()).path.notIn(permissionPaths));
                // 获取仍然保留的权限
                List<Permission> permissions = permissionService.select(permissionDef.groupId.eq(permissionGroup.getId()));
                dbPermissionMap = permissions.stream().collect(Collectors.toMap(Permission::getPath, permission -> permission));
            }

            List<Permission> insertPermission = new ArrayList<>();
            List<Permission> updatePermission = new ArrayList<>();
            for (PermissionGroupInit.Permission p : permissionGroupInit.getPermissions()) {
                Permission permission = dbPermissionMap.get(p.getPath());
                if (permission == null) {
                    permission = new Permission();
                    permission.setGroupId(permissionGroup.getId());
                    permission.setName(p.getName());
                    permission.setPath(p.getPath());
                    permission.setUserType(p.getUserType());
                    insertPermission.add(permission);
                } else if (!permission.getName().equals(p.getName()) || !permission.getUserType().equals(p.getUserType())) {
                    permission.setName(p.getName());
                    permission.setUserType(p.getUserType());
                    updatePermission.add(permission);
                }
            }
            if (!insertPermission.isEmpty()) {
                permissionService.insertBatch(insertPermission, 100);
            }
            if (!updatePermission.isEmpty()) {
                permissionService.updateBatchById(updatePermission, 100);
            }
        }
        InitMd5Utils.saveMd5String(InitMd5Keys.PERMISSION_INIT_MD5, md5);
        try {
            permissionGroupService.removeAllCache();
        }catch (Exception ignored){}
    }

}
