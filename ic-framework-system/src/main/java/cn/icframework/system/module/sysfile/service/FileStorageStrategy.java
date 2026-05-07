package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.config.FileStorageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 文件存储策略选择器
 *
 * @author ic
 * @since 2024/12/19
 */
@Component
@Slf4j
public class FileStorageStrategy {
    private final FileStorageConfig fileStorageConfig;
    private final Optional<IFileHelper> ossFileHelper;
    private final Optional<IFileHelper> minioFileHelper;
    private final DisabledFileHelper disabledFileHelper;
    private final boolean fileStorageEnabled;

    @Autowired
    public FileStorageStrategy(FileStorageConfig fileStorageConfig,
                               ObjectProvider<OssFileHelper> ossFileHelper,
                               ObjectProvider<MinioFileHelper> minioFileHelper,
                               DisabledFileHelper disabledFileHelper,
                               @Value("${ic.system.file-storage.enabled:true}") boolean fileStorageEnabled) {
        this.fileStorageConfig = fileStorageConfig;
        this.ossFileHelper = Optional.ofNullable(ossFileHelper.getIfAvailable()).map(IFileHelper.class::cast);
        this.minioFileHelper = Optional.ofNullable(minioFileHelper.getIfAvailable()).map(IFileHelper.class::cast);
        this.disabledFileHelper = disabledFileHelper;
        this.fileStorageEnabled = fileStorageEnabled;
    }

    /**
     * 获取当前配置的文件存储帮助类
     *
     * @return 文件存储帮助类
     */
    public IFileHelper getFileHelper() {
        if (!fileStorageEnabled) {
            log.warn("文件存储能力已关闭");
            return disabledFileHelper;
        }

        String storageType = fileStorageConfig.getType();
        log.info("当前文件存储类型: {}", storageType);

        if (storageType == null) {
            log.warn("存储类型为null，尝试使用默认OSS存储");
            return ossFileHelper.orElse(disabledFileHelper);
        }

        switch (storageType.toLowerCase()) {
            case "oss":
                return ossFileHelper.orElseGet(() -> {
                    log.warn("OSS配置不可用，回退到禁用文件存储");
                    return disabledFileHelper;
                });
            case "minio":
                if (minioFileHelper.isPresent()) {
                    return minioFileHelper.get();
                } else {
                    log.warn("MinIO配置不可用，尝试使用默认OSS存储");
                    return ossFileHelper.orElse(disabledFileHelper);
                }
            default:
                log.warn("未知的存储类型: {}，尝试使用默认OSS存储", storageType);
                return ossFileHelper.orElse(disabledFileHelper);
        }
    }

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    public String getStorageType() {
        return fileStorageConfig.getType();
    }

    /**
     * 是否启用分片上传
     *
     * @return 是否启用
     */
    public boolean isEnableSliceUpload() {
        return fileStorageConfig.isEnableSliceUpload();
    }

    /**
     * 获取分片大小
     *
     * @return 分片大小（字节）
     */
    public long getSliceSize() {
        return fileStorageConfig.getSliceSize();
    }
}
