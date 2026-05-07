package cn.icframework.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hzl
 * @since 2025/7/5
 */
@Configuration
@Deprecated(forRemoval = false)
@Import({
        SystemDataAutoConfiguration.class,
        SystemApiAutoConfiguration.class,
        SystemWebAutoConfiguration.class,
        SystemJobAutoConfiguration.class
})
public class SystemConfiguration {
}
