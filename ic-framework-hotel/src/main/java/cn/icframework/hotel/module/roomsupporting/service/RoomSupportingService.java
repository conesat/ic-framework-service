package cn.icframework.hotel.module.roomsupporting.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.roomsupporting.RoomSupporting;
import cn.icframework.hotel.module.roomsupporting.pojo.dto.RoomSupportingDTO;
import cn.icframework.hotel.module.roomsupporting.dao.RoomSupportingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/10/08
 */
@Service
public class RoomSupportingService extends BasicService<RoomSupportingMapper, RoomSupporting> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(RoomSupportingDTO dto) {
        RoomSupporting entity = dto.getId() != null ? selectById(dto.getId()) : RoomSupporting.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
