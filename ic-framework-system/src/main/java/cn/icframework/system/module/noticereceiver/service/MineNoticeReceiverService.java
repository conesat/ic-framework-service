package cn.icframework.system.module.noticereceiver.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.system.module.noticereceiver.NoticeReceiver;
import cn.icframework.system.module.noticereceiver.dao.NoticeReceiverMapper;
import cn.icframework.system.module.noticereceiver.def.NoticeReceiverDef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ic
 * @date 2024/09/13
 */
@Service
@RequiredArgsConstructor
public class MineNoticeReceiverService extends BasicService<NoticeReceiverMapper, NoticeReceiver> {

    /**
     * 标记已读
     *
     * @param noticeId
     * @param userId
     */
    public void read(Long userId, List<Long> ids) {
        update(NoticeReceiverDef.table().noticeId.in(ids).userId.eq(userId).readTime.set(LocalDateTime.now()));
    }

    /**
     * 删除-隐藏【因为仍需统计，只是用户端不显示】
     *
     * @param userId
     * @param ids
     */
    public void delete(Long userId, List<Long> ids) {
        update(NoticeReceiverDef.table().noticeId.in(ids).userId.eq(userId).hidden.set(true));
    }
}
