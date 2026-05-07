package cn.icframework.system.config;

import cn.icframework.system.init.PermissionInit;
import cn.icframework.system.module.monitor.config.SystemMonitorProperties;
import cn.icframework.system.module.ws.CacheWebSocketRouteStore;
import cn.icframework.system.module.ws.DefaultWebSocketSessionRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(prefix = "ic.system", name = "enabled", havingValue = "true", matchIfMissing = true)
@MapperScan({"cn.icframework.system.module.*.dao"})
@EnableConfigurationProperties({
        FileStorageConfig.class,
        MinioConfig.class,
        OssConfig.class,
        SystemMonitorProperties.class
})
@ComponentScan(
        basePackages = {
                "cn.icframework.system.common",
                "cn.icframework.system.module"
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SystemConfiguration.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = DefaultWebSocketSessionRegistry.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = CacheWebSocketRouteStore.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ANNOTATION,
                        classes = org.springframework.web.bind.annotation.RestController.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "cn\\.icframework\\.system\\.module\\..*\\.api\\..*"
                )
        }
)
@Import({
        CaptchaConfig.class,
        MinioConfiguration.class,
        PermissionInit.class
})
public class SystemDataAutoConfiguration {
}
