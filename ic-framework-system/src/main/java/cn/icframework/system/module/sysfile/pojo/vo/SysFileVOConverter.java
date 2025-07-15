package cn.icframework.system.module.sysfile.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.core.utils.FileUtils;
import cn.icframework.system.module.sysfile.SysFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author create by ic gen
 * @date 2023/06/21
 */
@Component
@RequiredArgsConstructor
public class SysFileVOConverter extends BasicConverter<SysFile, SysFileVO> {


    @Override
    public void convert(SysFileVO dto, SysFile entity) {
        dto.setSizeTip(FileUtils.getNetFileSizeDescription(entity.getSize()));
        dto.setUrl(entity.getBucketUrl() + "/" + entity.getOssObjectName());
    }
}
