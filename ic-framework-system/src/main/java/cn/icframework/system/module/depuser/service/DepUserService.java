package cn.icframework.system.module.depuser.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.depuser.DepUser;
import cn.icframework.system.module.depuser.def.DepUserDef;
import cn.icframework.system.module.depuser.pojo.dto.DepUserDTO;
import cn.icframework.system.module.depuser.dao.DepUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author ic
 * @date 2024/08/11
 */
@Service
public class DepUserService extends BasicService<DepUserMapper, DepUser> {

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(DepUserDTO dto) {
        DepUser entity = dto.getId() != null ? selectById(dto.getId()) : DepUser.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Transactional
    public void createBatch(Long depId, Long[] userIds) {
        List<DepUser> depUsers = new ArrayList<>();
        for (Long userId : userIds) {
            DepUser depUser = DepUser.def();
            depUser.setUserId(userId);
            depUser.setDepId(depId);
            depUsers.add(depUser);
        }
        if (!depUsers.isEmpty()) {
            insertBatch(depUsers);
        }
    }

    /**
     * 设置部门用户id
     *
     * @param id
     */
    @Transactional
    public void setManager(DepUser depUser, Boolean manager) {
        if (depUser.getManager() == manager) {
            return;
        }
        DepUserDef depUserDef = DepUserDef.table();
        update(UPDATE(depUserDef).SET(depUserDef.manager, false).WHERE(depUserDef.depId.eq(depUser.getDepId())));
        update(UPDATE(depUserDef).SET(depUserDef.manager, manager).WHERE(depUserDef.id.eq(depUser.getId())));
    }

    /**
     * 设置部门用户id
     *
     * @param depId
     * @param userId
     */
    @Transactional
    public void setManager(Long depId, Long userId, Boolean manager) {
        DepUserDef depUserDef = DepUserDef.table();
        DepUser depUser = selectOne(depUserDef.depId.eq(depId).userId.eq(userId));
        if (depUser == null) {
            depUser = DepUser.def();
            depUser.setDepId(depId);
            depUser.setUserId(userId);
            insert(depUser);
        }
        setManager(depUser, manager);
    }


    /**
     * 编辑用户部门
     * @param userId
     * @param depIds
     * @param update
     */
    @Transactional
    public void setUserDep(Long userId, Long[] depIds, boolean update) {
        HashSet<Long> dbDepIdSet = null;

        if (update) {
            DepUserDef depUserDef = DepUserDef.table();
            List<Long> dbDepIds = select(SELECT(depUserDef.depId).FROM(depUserDef).WHERE(depUserDef.userId.eq(userId)), Long.class);
            dbDepIdSet = new HashSet<>(dbDepIds);
            if (depIds != null && depIds.length > 0) {
                delete(depUserDef.userId.eq(userId).depId.notIn(depIds));
            } else {
                delete(depUserDef.userId.eq(userId));
            }
        }
        if (depIds != null && depIds.length > 0) {
            List<DepUser> depUsers = new ArrayList<>();
            for (Long depId : depIds) {
                if (dbDepIdSet != null && dbDepIdSet.contains(depId)) {
                    continue;
                }
                DepUser depUser = DepUser.def();
                depUser.setUserId(userId);
                depUser.setDepId(depId);
                depUsers.add(depUser);
            }

            if (!depUsers.isEmpty()) {
                insertBatch(depUsers, false, false);
            }
        }
    }
}
