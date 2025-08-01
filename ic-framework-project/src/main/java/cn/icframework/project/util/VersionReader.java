package cn.icframework.project.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 版本信息读取器
 * 用于读取resources目录下的version文件
 */
@Component
public class VersionReader {
    
    /**
     * 读取版本信息
     * @return 版本信息JSON对象
     */
    public JSONObject readVersion() {
        try {
            ClassPathResource resource = new ClassPathResource("version");
            try (InputStream inputStream = resource.getInputStream()) {
                String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                return JSON.parseObject(content);
            }
        } catch (IOException e) {
            // 如果读取失败，返回默认值
            JSONObject defaultVersion = new JSONObject();
            defaultVersion.put("version", "unknown");
            defaultVersion.put("timestamp", "unknown");
            return defaultVersion;
        }
    }
    
    /**
     * 获取版本号
     * @return 版本号
     */
    public String getVersion() {
        JSONObject versionInfo = readVersion();
        return versionInfo.getString("version");
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public String getTimestamp() {
        JSONObject versionInfo = readVersion();
        return versionInfo.getString("timestamp");
    }
} 