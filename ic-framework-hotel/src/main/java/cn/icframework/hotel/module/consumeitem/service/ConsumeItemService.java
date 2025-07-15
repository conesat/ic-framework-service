package cn.icframework.hotel.module.consumeitem.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.consumeitem.ConsumeItem;
import cn.icframework.hotel.module.consumeitem.dao.ConsumeItemMapper;
import cn.icframework.hotel.module.consumeitem.pojo.dto.ConsumeItemDTO;
import cn.icframework.system.module.sysfile.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author ic
 * @date 2024/10/14
 */
@Service
@RequiredArgsConstructor
public class ConsumeItemService extends BasicService<ConsumeItemMapper, ConsumeItem> {

    private final SysFileService sysFileService;

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(ConsumeItemDTO dto) {
        ConsumeItem entity = dto.getId() != null ? selectById(dto.getId()) : ConsumeItem.def();
        Long dbFileId = entity.getFileId();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            // 删除旧图片
            if (!Objects.equals(dbFileId, entity.getFileId())) {
                sysFileService.removeRef(dbFileId);
                sysFileService.addRef(entity.getFileId());
            }
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Override
    public void after(SqlCommandType sqlCommandType, ConsumeItem entity) {
        if (sqlCommandType == SqlCommandType.DELETE) {
            sysFileService.removeRef(entity.getFileId());
        } else if (sqlCommandType == SqlCommandType.INSERT) {
            sysFileService.addRef(entity.getFileId());
        }
    }
}
