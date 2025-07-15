package cn.icframework.hotel.module.timescope.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.timescope.TimeScope;
import cn.icframework.hotel.module.timescope.pojo.dto.TimeScopeDTO;
import cn.icframework.hotel.module.timescope.dao.TimeScopeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/11/26
 */
@Service
public class TimeScopeService extends BasicService<TimeScopeMapper, TimeScope> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(TimeScopeDTO dto) {
        TimeScope entity = dto.getId() != null ? selectById(dto.getId()) : TimeScope.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
