package cn.icframework.system.module.noticereceiver.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.notice.pojo.dto.NoticeDTO;
import cn.icframework.system.module.notice.wrapperbuilder.NoticeWrapperBuilder;
import cn.icframework.system.module.noticereceiver.NoticeReceiver;
import cn.icframework.system.module.noticereceiver.dao.NoticeReceiverMapper;
import cn.icframework.system.module.noticereceiver.def.NoticeReceiverDef;
import cn.icframework.system.module.noticereceiver.pojo.dto.NoticeReceiverDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ic
 * @since 2024/09/13
 */
@Service
@RequiredArgsConstructor
public class NoticeReceiverService extends BasicService<NoticeReceiverMapper, NoticeReceiver> {
    private final NoticeWrapperBuilder wrapperBuilder;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(NoticeReceiverDTO dto) {
        NoticeReceiver entity = dto.getId() != null ? selectById(dto.getId()) : new NoticeReceiver();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Async
    public void sendNotice(NoticeDTO dto) {
        // 针对人员分发
        if (dto.getReceiveUserIds() != null) {
            addUserBatch(dto.getId(), dto.getReceiveUserIds());
        }
        // 针对部门分发
        if (dto.getReceiveDepIds() != null) {
            insert(wrapperBuilder.distributeDep(dto.getId(), dto.getReceiveDepIds()));
        }
        // 针对角色分发
        if (dto.getReceiveRoleIds() != null) {
            insert(wrapperBuilder.distributeRole(dto.getId(), dto.getReceiveRoleIds()));
        }
        // 针对岗位分发
        if (dto.getReceivePosIds() != null) {
            insert(wrapperBuilder.distributePos(dto.getId(), dto.getReceivePosIds()));
        }
    }

    /**
     * 批量添加通知用户
     *
     * @param noticeId
     * @param userIds
     */
    public void addUserBatch(Long noticeId, Long[] userIds) {
        if (userIds == null || userIds.length == 0) {
            return;
        }
        insert(wrapperBuilder.distributeUser(noticeId, userIds));
    }

    /**
     * 批量删除
     *
     * @param noticeId
     * @param userIds
     */
    public void delete(Long noticeId, List<Long> userIds) {
        NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
        delete(noticeReceiverDef.noticeId.eq(noticeId).userId.in(userIds));
    }

}
