package cn.icframework.project.module.energy.swapbattery.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.project.module.energy.swapbattery.SwapBattery;
import cn.icframework.project.module.energy.swapbattery.pojo.dto.SwapBatteryDTO;
import cn.icframework.project.module.energy.swapbattery.dao.SwapBatteryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author ic
 * @since 2026/07/13
 */
@Service
public class SwapBatteryService extends BasicService<SwapBatteryMapper, SwapBattery> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(SwapBatteryDTO dto) {
        SwapBattery entity = dto.getId() != null ? selectById(dto.getId()) : new SwapBattery();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
