package cn.icframework.system.module.dep.service;

import cn.icframework.common.utils.ConvertUtils;
import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.dep.Dept;
import cn.icframework.system.module.dep.dao.DeptMapper;
import cn.icframework.system.module.dep.pojo.dto.DeptDTO;
import cn.icframework.system.module.depuser.def.DepUserDef;
import cn.icframework.system.module.depuser.service.DepUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author create by ic gen
 * @since 2023/06/21
 */
@Service
@RequiredArgsConstructor
public class DeptService extends BasicService<DeptMapper, Dept> {

    private final DepUserService depUserService;

    /**
     * 编辑或者保存
     *
     * @param form
     * @param parentId
     */
    @Transactional
    public void edit(DeptDTO form) {
        Assert.isFalse(form.getParentId() != null && Objects.equals(form.getId(), form.getParentId()), "父级不能是自己");
        if (form.getParentId() != null) {
            Dept parent = selectById(form.getParentId());
            Assert.isNotNull(parent, "上级部门不存在");
            if (form.getId() != null) {
                Assert.isFalse(parent.getParentPath().contains(Dept.PATH_CHAR + form.getId() + Dept.PATH_CHAR), "父级不能是当前下级");
            }
            if (StringUtils.isEmpty(parent.getParentPath())) {
                form.setParentPath(Dept.PATH_CHAR + form.getParentId() + Dept.PATH_CHAR);
            } else {
                form.setParentPath(ConvertUtils.toString(parent.getParentPath(), "") + form.getParentId() + Dept.PATH_CHAR);
            }
        }
        Dept entity = form.getId() != null ? selectById(form.getId()) : new Dept();
        BeanUtils.copyExcludeProps(form, entity, Dept::getCreateTime);
        if (form.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
        if (form.getLeaderUserId() != null) {
            depUserService.setManager(entity.getId(), form.getLeaderUserId(), true);
        }
    }

    @Override
    public void after(SqlCommandType sqlCommandType, Dept entity) {
        super.after(sqlCommandType, entity);
        DepUserDef depUserDef = DepUserDef.table();
        if (sqlCommandType == SqlCommandType.DELETE) {
            // 部门删除，移除所有部门用户
            depUserService.delete(depUserDef.depId.eq(entity.getId()));
        }
    }
}
