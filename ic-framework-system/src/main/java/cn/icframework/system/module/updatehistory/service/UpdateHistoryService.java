package cn.icframework.system.module.updatehistory.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.updatehistory.UpdateHistory;
import cn.icframework.system.module.updatehistory.pojo.dto.UpdateHistoryDTO;
import cn.icframework.system.module.updatehistory.dao.UpdateHistoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @author ic
 * @since 2025/07/31
 */
@Service
@RequiredArgsConstructor
public class UpdateHistoryService extends BasicService<UpdateHistoryMapper, UpdateHistory> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(UpdateHistoryDTO dto) {
        UpdateHistory entity = dto.getId() != null ? selectById(dto.getId()) : new UpdateHistory();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
