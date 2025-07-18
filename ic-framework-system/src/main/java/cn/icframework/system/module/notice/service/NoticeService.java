package cn.icframework.system.module.notice.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.notice.Notice;
import cn.icframework.system.module.notice.dao.NoticeMapper;
import cn.icframework.system.module.notice.pojo.dto.NoticeDTO;
import cn.icframework.system.module.noticereceiver.service.NoticeReceiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @since 2024/09/12
 */
@Service
@RequiredArgsConstructor
public class NoticeService extends BasicService<NoticeMapper, Notice> {
    private final NoticeReceiverService noticeReceiverService;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(NoticeDTO dto) {
        Notice entity = dto.getId() != null ? selectById(dto.getId()) : new Notice();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
        noticeReceiverService.sendNotice(dto);
    }

}
