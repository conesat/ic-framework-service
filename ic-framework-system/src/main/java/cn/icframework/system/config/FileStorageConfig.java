package cn.icframework.system.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储配置类
 * @author ic
 * @since 2024/12/19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.file-storage")
public class FileStorageConfig {
    /**
     * 存储类型：oss 或 fastdfs
     */
    private String type = "oss";
    
    /**
     * 是否启用分片上传
     */
    private boolean enableSliceUpload = true;
    
    /**
     * 分片大小（字节）
     */
    private long sliceSize = 5 * 1024 * 1024; // 5MB
} 