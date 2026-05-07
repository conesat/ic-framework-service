package cn.icframework.system.module.sysfile.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.config.OssConfig;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.dao.SysFileMapper;
import cn.icframework.system.module.sysfile.pojo.dto.SysFileDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.Optional;

/**
 * @author create by ic gen
 * @since 2023/06/21
 */
@Service
@RequiredArgsConstructor
public class SysFileService extends BasicService<SysFileMapper, SysFile> {

    private final Optional<OssConfig> ossConfig;
    @Value("${ic.system.file-storage.enabled:true}")
    private boolean fileStorageEnabled;


    @Override
    public void before(SqlCommandType sqlCommandType, SysFile entity) {
        if (sqlCommandType == SqlCommandType.DELETE) {
            entity.setRefCount(entity.getRefCount() - 1);
        }
    }

    @Override
    public void after(SqlCommandType sqlCommandType, SysFile entity) {
        if (sqlCommandType == SqlCommandType.DELETE || sqlCommandType == SqlCommandType.UPDATE) {
            if (entity.getRefCount() <= 0) {
                if (fileStorageEnabled && ossConfig.isPresent() && ossConfig.get().getOss() != null) {
                    ossConfig.get().getOss().deleteObject(entity.getBucketName(), entity.getOssObjectName());
                }
            }
        }
    }

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(SysFileDTO dto) {
        SysFile entity = dto.getId() != null ? selectById(dto.getId()) : new SysFile();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }


    @Transactional
    public void removeRef(Long id) {
        if (id == null) {
            return;
        }
        SysFile sysFile = selectById(id);
        if (sysFile != null) {
            sysFile.setRefCount(sysFile.getRefCount() - 1);
            if (sysFile.getRefCount() <= 0) {
                deleteById(id);
            } else {
                updateById(sysFile);
            }
        }
    }

    @Transactional
    public void addRef(Long id) {
        if (id == null) {
            return;
        }
        SysFile sysFile = selectById(id);
        if (sysFile != null) {
            sysFile.setRefCount(sysFile.getRefCount() + 1);
            updateById(sysFile);
        }
    }

    public String generatePreviewUrl(String bucketName, String objectName) {
        if (!fileStorageEnabled || ossConfig.isEmpty() || ossConfig.get().getOss() == null) {
            return "";
        }
        // 设置签名URL过期时间，单位为毫秒。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        URL url = ossConfig.get().getOss().generatePresignedUrl(bucketName, objectName, expiration);
        return url == null ? "" : url.toString();
    }
}
