package cn.icframework.system.module.role.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.userrole.def.UserRoleDef;
import cn.icframework.system.module.userrole.service.UserRoleService;
import cn.icframework.system.module.role.Role;
import cn.icframework.system.module.role.dao.RoleMapper;
import cn.icframework.system.module.role.def.RoleDef;
import cn.icframework.system.module.role.pojo.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author create by ic gen
 * @date 2023/06/20
 */
@Service
@RequiredArgsConstructor
public class RoleService extends BasicService<RoleMapper, Role> {
    private final UserRoleService userRoleService;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(RoleDTO dto) {
        Role entity = dto.getId() != null ? selectById(dto.getId()) : Role.def();
        BeanUtils.copyExcludeProps(dto, entity, Role::getCreateTime);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Override
    public void before(SqlCommandType sqlCommandType, Role entity) {
        if (sqlCommandType == SqlCommandType.DELETE) {
            Assert.isFalse(entity.isSystem(), "系统角色不允许删除");
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
            Assert.isFalse(entity.isSystem(), "系统角色不允许编辑");
        }

        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            RoleDef roleDef = RoleDef.table();
            Role role = selectOne(SELECT().FROM(roleDef).WHERE(roleDef.id.ne(entity.getId()), AND(roleDef.sign.eq(entity.getSign()).or().name.eq(entity.getName()))));
            if (role != null) {
                Assert.isFalse(role.getName().equals(entity.getName()), "角色名已存在");
                Assert.isFalse(role.getSign().equals(entity.getSign()), "角色标识已存在");
            }
        }
    }

    @Override
    public void after(SqlCommandType sqlCommandType, Role entity) {
        if (sqlCommandType == SqlCommandType.DELETE) {
            UserRoleDef userRoleDef = UserRoleDef.table();
            userRoleService.delete(userRoleDef.roleId.eq(entity.getId()));
        }
    }
}
