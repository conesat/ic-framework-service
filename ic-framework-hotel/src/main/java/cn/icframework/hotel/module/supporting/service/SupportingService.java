package cn.icframework.hotel.module.supporting.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.supporting.Supporting;
import cn.icframework.hotel.module.supporting.dao.SupportingMapper;
import cn.icframework.hotel.module.supporting.pojo.dto.SupportingDTO;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author ic
 * @date 2024/09/25
 */
@Service
@RequiredArgsConstructor
public class SupportingService extends BasicService<SupportingMapper, Supporting> {
    private final SysFileService sysFileService;

    /**
     * 编辑或者保存
     * @param dto
     */
    @Transactional
    public void edit(SupportingDTO dto) {
        Supporting entity = dto.getId() != null ? selectById(dto.getId()) : Supporting.def();
        Long picFileIdOld = entity.getIconFileId();
        BeanUtils.copyExcludeProps(dto, entity);
        // 新增或者变化调整icon地址
        if (dto.getId() == null || !Objects.equals(picFileIdOld, entity.getIconFileId())) {
            SysFile sysFile = sysFileService.selectById(entity.getIconFileId());
            if (sysFile != null) {
                entity.setIconFileUrl(sysFile.getBucketUrl() + "/" + sysFile.getOssObjectName());
            }
        }
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
        if (!Objects.equals(picFileIdOld, entity.getIconFileId())) {
            sysFileService.removeRef(picFileIdOld);
            if (entity.getIconFileId() != null) {
                sysFileService.addRef(entity.getIconFileId());
            }
        }
    }
}
