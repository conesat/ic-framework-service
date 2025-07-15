package cn.icframework.system.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author hzl
 * @date 2023/6/21
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.oss")
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    private OSS oss;

    @PostConstruct
    void init() {
        if (StringUtils.hasLength(accessKeyId)) {
            oss = new OSSClientBuilder().build("https://" + endpoint, accessKeyId, accessKeySecret);
        }
    }

}
