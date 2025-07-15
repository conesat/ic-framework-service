package cn.icframework.hotel.module.hotel.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.hotel.Hotel;
import cn.icframework.hotel.module.hotel.dao.HotelMapper;
import cn.icframework.hotel.module.hotel.pojo.dto.HotelDTO;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author ic
 * @date 2024/09/24
 */
@Service
@RequiredArgsConstructor
public class HotelService extends BasicService<HotelMapper, Hotel> {
    private final SysFileService sysFileService;
    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(HotelDTO dto) {
        Hotel entity = dto.getId() != null ? selectById(dto.getId()) : Hotel.def();
        Long picFileIdOld = entity.getPicFileId();
        BeanUtils.copyExcludeProps(dto, entity);
        // 新增或者变化调整图片地址
        if (dto.getId() == null || !Objects.equals(picFileIdOld, entity.getPicFileId())) {
            SysFile sysFile = sysFileService.selectById(entity.getPicFileId());
            if (sysFile != null) {
                entity.setPicFileUrl(sysFile.getBucketUrl() + "/" + sysFile.getOssObjectName());
            }
        }
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
        if (!Objects.equals(picFileIdOld, entity.getPicFileId())) {
            sysFileService.removeRef(picFileIdOld);
            if (entity.getPicFileId() != null) {
                sysFileService.addRef(entity.getPicFileId());
            }
        }
    }
}
