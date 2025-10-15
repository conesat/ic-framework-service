package cn.icframework.system.module.updatehistory.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.updatehistory.UpdateHistory;
import cn.icframework.system.module.updatehistory.pojo.dto.UpdateHistoryDTO;
import cn.icframework.system.module.updatehistory.dao.UpdateHistoryMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.version:unknown}")
    private String appVersion;

    /**
     * 更新系统
     *
     * @param form    [UpdateHistoryDTO] 更新历史表单
     * @param subject [String] 操作人
     * @return
     */
    public void updateSystem(@Valid UpdateHistoryDTO form, String subject) {
        UpdateHistory updateHistory = new UpdateHistory();
        updateHistory.setRemark(form.getRemark());
        updateHistory.setPersonName(form.getPersonName());
        updateHistory.setFileId(form.getFileId());
        updateHistory.setUserId(Long.valueOf(subject));

    }
}
