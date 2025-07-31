package cn.icframework.system.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * FastDFS配置类
 * @author ic
 * @since 2024/12/19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.fastdfs")
public class FastDfsConfig {
    /**
     * FastDFS服务器地址
     */
    private String serverUrl;
    
    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 5000;
    
    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 10000;
    
    /**
     * 连接池最大连接数
     */
    private int maxConnections = 50;
    
    /**
     * 连接池最大空闲连接数
     */
    private int maxIdleConnections = 10;
} 