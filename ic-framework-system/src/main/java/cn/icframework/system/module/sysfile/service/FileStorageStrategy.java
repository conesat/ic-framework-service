package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.config.FileStorageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final OssFileHelper ossFileHelper;
    private final Optional<FastDfsFileHelper> fastDfsFileHelper;

    @Autowired
    public FileStorageStrategy(FileStorageConfig fileStorageConfig,
                               @Autowired(required = false) OssFileHelper ossFileHelper,
                               @Autowired(required = false) FastDfsFileHelper fastDfsFileHelper) {
        this.fileStorageConfig = fileStorageConfig;
        this.ossFileHelper = ossFileHelper;
        this.fastDfsFileHelper = Optional.ofNullable(fastDfsFileHelper);
    }

    /**
     * 获取当前配置的文件存储帮助类
     *
     * @return 文件存储帮助类
     */
    public IFileHelper getFileHelper() {
        String storageType = fileStorageConfig.getType();
        log.info("当前文件存储类型: {}", storageType);

        if (storageType == null) {
            log.warn("存储类型为null，使用默认OSS存储");
            return ossFileHelper;
        }

        switch (storageType.toLowerCase()) {
            case "oss":
                return ossFileHelper;
            case "fastdfs":
                if (fastDfsFileHelper.isPresent()) {
                    return fastDfsFileHelper.get();
                } else {
                    log.warn("FastDFS配置不可用，使用默认OSS存储");
                    return ossFileHelper;
                }
            default:
                log.warn("未知的存储类型: {}，使用默认OSS存储", storageType);
                return ossFileHelper;
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