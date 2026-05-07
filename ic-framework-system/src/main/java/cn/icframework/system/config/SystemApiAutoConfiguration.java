package cn.icframework.system.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "ic.system", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackages = "cn.icframework.system.module", includeFilters = @ComponentScan.Filter(org.springframework.web.bind.annotation.RestController.class), useDefaultFilters = false)
public class SystemApiAutoConfiguration {
}
