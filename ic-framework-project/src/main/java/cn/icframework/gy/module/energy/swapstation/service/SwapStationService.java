package cn.icframework.project.module.energy.swapstation.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.project.module.energy.swapstation.SwapStation;
import cn.icframework.project.module.energy.swapstation.pojo.dto.SwapStationDTO;
import cn.icframework.project.module.energy.swapstation.dao.SwapStationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author ic
 * @since 2026/07/13
 */
@Service
public class SwapStationService extends BasicService<SwapStationMapper, SwapStation> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(SwapStationDTO dto) {
        SwapStation entity = dto.getId() != null ? selectById(dto.getId()) : new SwapStation();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
