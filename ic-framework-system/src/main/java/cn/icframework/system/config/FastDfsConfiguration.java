package cn.icframework.system.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * FastDFS配置类
 *
 * @author ic
 * @since 2024/12/19
 */
@Configuration
@ConditionalOnProperty(name = "app.file-storage.type", havingValue = "fastdfs", matchIfMissing = false)
@Import({FdfsClientConfig.class})
@EnableConfigurationProperties(FastDfsConfig.class)
public class FastDfsConfiguration {

    static {
        // 设置FastDFS连接超时时间
        System.setProperty("fdfs.so-timeout", "10000");
        System.setProperty("fdfs.connect-timeout", "5000");
    }
}