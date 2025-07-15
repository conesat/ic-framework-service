package cn.icframework.hotel.module.roomprice.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.roomprice.RoomPrice;
import cn.icframework.hotel.module.roomprice.pojo.dto.RoomPriceDTO;
import cn.icframework.hotel.module.roomprice.dao.RoomPriceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/10/14
 */
@Service
public class RoomPriceService extends BasicService<RoomPriceMapper, RoomPrice> {

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(RoomPriceDTO dto) {
        RoomPrice entity = dto.getId() != null ? selectById(dto.getId()) : RoomPrice.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }
}
