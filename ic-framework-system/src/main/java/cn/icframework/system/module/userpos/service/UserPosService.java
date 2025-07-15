package cn.icframework.system.module.userpos.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.userpos.UserPos;
import cn.icframework.system.module.userpos.dao.UserPosMapper;
import cn.icframework.system.module.userpos.def.UserPosDef;
import cn.icframework.system.module.userpos.pojo.dto.UserPosDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author ic
 * @date 2024/09/09
 */
@Service
public class UserPosService extends BasicService<UserPosMapper, UserPos> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(UserPosDTO dto) {
        UserPos entity = dto.getId() != null ? selectById(dto.getId()) : UserPos.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }


    /**
     * 编辑用户岗位
     * @param userId
     * @param posIds
     * @param update
     */
    @Transactional
    public void setUserPos(Long userId, Long[] posIds, boolean update) {
        HashSet<Long> dbPosIdSet = null;

        if (update) {
            UserPosDef userPosDef = UserPosDef.table();
            List<Long> dbDepIds = select(SELECT(userPosDef.positionId).FROM(userPosDef).WHERE(userPosDef.userId.eq(userId)), Long.class);
            dbPosIdSet = new HashSet<>(dbDepIds);
            if (posIds != null && posIds.length > 0) {
                delete(userPosDef.userId.eq(userId).positionId.notIn(posIds));
            } else {
                delete(userPosDef.userId.eq(userId));
            }
        }
        if (posIds != null && posIds.length > 0) {
            List<UserPos> userPosList = new ArrayList<>();
            for (Long posId : posIds) {
                if (dbPosIdSet != null && dbPosIdSet.contains(posId)) {
                    continue;
                }
                UserPos userPos = UserPos.def();
                userPos.setUserId(userId);
                userPos.setPositionId(posId);
                userPosList.add(userPos);
            }

            if (!userPosList.isEmpty()) {
                insertBatch(userPosList, false, false);
            }
        }
    }


    @Transactional
    public void createBatch(Long posId, Long[] userIds) {
        List<UserPos> userPosList = new ArrayList<>();
        for (Long userId : userIds) {
            UserPos userPos = UserPos.def();
            userPos.setUserId(userId);
            userPos.setPositionId(posId);
            userPosList.add(userPos);
        }
        if (!userPosList.isEmpty()) {
            insertBatch(userPosList);
        }
    }
}
