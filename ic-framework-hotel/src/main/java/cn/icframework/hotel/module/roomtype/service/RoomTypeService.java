package cn.icframework.hotel.module.roomtype.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.roomtype.RoomType;
import cn.icframework.hotel.module.roomtype.pojo.dto.RoomTypeDTO;
import cn.icframework.hotel.module.roomtype.dao.RoomTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/09/25
 */
@Service
public class RoomTypeService extends BasicService<RoomTypeMapper, RoomType> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(RoomTypeDTO dto) {
        RoomType entity = dto.getId() != null ? selectById(dto.getId()) : RoomType.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
