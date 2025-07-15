package cn.icframework.hotel.module.building.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.building.Building;
import cn.icframework.hotel.module.building.pojo.dto.BuildingDTO;
import cn.icframework.hotel.module.building.dao.BuildingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/09/25
 */
@Service
public class BuildingService extends BasicService<BuildingMapper, Building> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(BuildingDTO dto) {
        Building entity = dto.getId() != null ? selectById(dto.getId()) : Building.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
