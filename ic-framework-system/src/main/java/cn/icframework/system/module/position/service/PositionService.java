package cn.icframework.system.module.position.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.position.Position;
import cn.icframework.system.module.position.pojo.dto.PositionDTO;
import cn.icframework.system.module.position.dao.PositionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/09/09
 */
@Service
public class PositionService extends BasicService<PositionMapper, Position> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(PositionDTO dto) {
        Position entity = dto.getId() != null ? selectById(dto.getId()) : Position.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
