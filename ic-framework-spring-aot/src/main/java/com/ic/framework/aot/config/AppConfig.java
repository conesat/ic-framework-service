package com.ic.framework.aot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 应用程序配置类
 * 
 * @author IC Framework
 * @version 1.0.0
 */
@Configuration
public class AppConfig {

    /**
     * 应用程序信息 Bean
     */
    @Bean
    public Map<String, Object> appInfo() {
        return Map.of(
            "name", "IC Framework Spring AOT",
            "version", "1.0.0",
            "description", "Spring Boot 3.5.3 AOT 原生镜像示例",
            "startTime", LocalDateTime.now(),
            "features", new String[]{"AOT", "Native Image", "Spring Boot 3.5.3"}
        );
    }

    /**
     * 应用程序配置属性
     */
    @Bean
    @ConfigurationProperties(prefix = "app")
    public AppProperties appProperties() {
        return new AppProperties();
    }

    /**
     * 应用程序属性类
     */
    public static class AppProperties {
        private String name = "IC Framework Spring AOT";
        private String version = "1.0.0";
        private String environment = "development";

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }
    }
} 