package cn.icframework.system.config;

import io.minio.MinioClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * MinIO配置类
 * @author ic
 * @since 2024/12/19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.minio")
public class MinioConfig {
    /**
     * MinIO服务器地址
     */
    private String endpoint;
    
    /**
     * 访问密钥
     */
    private String accessKey;
    
    /**
     * 秘密密钥
     */
    private String secretKey;
    
    /**
     * 默认存储桶
     */
    private String bucketName = "ic-framework";
    
    /**
     * 是否使用HTTPS
     */
    private boolean secure = false;
}

@Configuration
class MinioConfiguration {
    
    @Bean
    public MinioClient minioClient(MinioConfig minioConfig) {
        return MinioClient.builder()
                .endpoint(minioConfig.getEndpoint())
                .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                .build();
    }
} 