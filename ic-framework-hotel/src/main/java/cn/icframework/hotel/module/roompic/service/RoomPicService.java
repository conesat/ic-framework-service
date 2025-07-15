package cn.icframework.hotel.module.roompic.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.roompic.RoomPic;
import cn.icframework.hotel.module.roompic.dao.RoomPicMapper;
import cn.icframework.hotel.module.roompic.pojo.dto.RoomPicDTO;
import cn.icframework.system.module.sysfile.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ic
 * @date 2024/10/10
 */
@Service
@RequiredArgsConstructor
public class RoomPicService extends BasicService<RoomPicMapper, RoomPic> {
    private final SysFileService sysFileService;

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(RoomPicDTO dto) {
        RoomPic entity = dto.getId() != null ? selectById(dto.getId()) : RoomPic.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Override
    public void after(SqlCommandType sqlCommandType, RoomPic entity) {
        if (sqlCommandType == SqlCommandType.DELETE) {
            sysFileService.removeRef(entity.getFileId());
        } else if (sqlCommandType == SqlCommandType.INSERT) {
            sysFileService.addRef(entity.getFileId());
        }
    }
}
