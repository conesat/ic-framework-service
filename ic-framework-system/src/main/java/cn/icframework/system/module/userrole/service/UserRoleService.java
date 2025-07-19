package cn.icframework.system.module.userrole.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.role.def.RoleDef;
import cn.icframework.system.module.userrole.UserRole;
import cn.icframework.system.module.userrole.dao.UserRoleMapper;
import cn.icframework.system.module.userrole.def.UserRoleDef;
import cn.icframework.system.module.userrole.pojo.dto.UserRoleDTO;
import cn.icframework.system.module.userrole.wrapperbuilder.UserRoleWrapperBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author ic generator
 * @since 2023/08/09
 */
@Service
@RequiredArgsConstructor
public class UserRoleService extends BasicService<UserRoleMapper, UserRole> {
    private final UserRoleWrapperBuilder userRoleWrapperBuilder;

    /**
     * 编辑或者保存
     *
     * @param dto 用户角色输入参数
     */
    @Transactional
    public void edit(UserRoleDTO dto) {
        UserRole entity = dto.getId() != null ? selectById(dto.getId()) : new UserRole();
        BeanUtils.copyExcludeProps(dto, entity);
        if (StringUtils.hasLength(dto.getId())) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    /**
     * 获取用户是否具备超管角色
     *
     * @param userId 用户id
     * @return 是否超管
     */
    public boolean isSu(Long userId) {
        UserRoleDef userRoleDef = UserRoleDef.table();
        RoleDef roleDef = RoleDef.table();
        UserRole userRole = selectOne(SELECT().FROM(userRoleDef)
                .LEFT_JOIN(roleDef).ON(userRoleDef.roleId.eq(roleDef.id))
                .WHERE(userRoleDef.userId.eq(userId), roleDef.su.eq(true)));
        return userRole != null;
    }

    /**
     * 编辑用户角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     * @param update  是否更新
     */
    @Transactional
    public void setUserRole(Long userId, Long[] roleIds, boolean update) {
        HashSet<Long> dbRoleIdSet = null;

        if (update) {
            UserRoleDef userRoleDef = UserRoleDef.table();
            List<Long> dbRoleIds = select(SELECT(userRoleDef.roleId).FROM(userRoleDef).WHERE(userRoleDef.userId.eq(userId)), Long.class);
            dbRoleIdSet = new HashSet<>(dbRoleIds);
            if (roleIds != null && roleIds.length > 0) {
                delete(userRoleDef.userId.eq(userId).roleId.notIn(roleIds));
            } else {
                delete(userRoleDef.userId.eq(userId));
            }
        }
        if (roleIds != null && roleIds.length > 0) {
            List<UserRole> userRoles = new ArrayList<>();
            for (Long roleId : roleIds) {
                if (dbRoleIdSet != null && dbRoleIdSet.contains(roleId)) {
                    continue;
                }
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoles.add(userRole);
            }

            if (!userRoles.isEmpty()) {
                insertBatch(userRoles, false, false);
            }
        }
    }
}
