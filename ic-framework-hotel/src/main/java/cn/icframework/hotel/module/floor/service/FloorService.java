package cn.icframework.hotel.module.floor.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.floor.Floor;
import cn.icframework.hotel.module.floor.dao.FloorMapper;
import cn.icframework.hotel.module.floor.def.FloorDef;
import cn.icframework.hotel.module.floor.pojo.dto.FloorDTO;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/09/25
 */
@Service
public class FloorService extends BasicService<FloorMapper, Floor> {

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(FloorDTO dto) {
        Floor entity = dto.getId() != null ? selectById(dto.getId()) : Floor.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Override
    public void before(SqlCommandType sqlCommandType, Floor entity) {
        FloorDef floorDef = FloorDef.table();
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            Integer dbName = selectOne(
                    SELECT(1)
                            .FROM(floorDef)
                            .WHERE(floorDef.id.ne(entity.getId()).buildingId.eq(entity.getBuildingId()).name.eq(entity.getName()))
                    , Integer.class);
            Assert.isNull(dbName, "楼层名称已存在");
            Integer dbLevel = selectOne(
                    SELECT(1)
                            .FROM(floorDef)
                            .WHERE(floorDef.id.ne(entity.getId()).buildingId.eq(entity.getBuildingId()).level.eq(entity.getLevel()))
                    , Integer.class);
            Assert.isNull(dbLevel, "层级已存在");
        }
    }
}
