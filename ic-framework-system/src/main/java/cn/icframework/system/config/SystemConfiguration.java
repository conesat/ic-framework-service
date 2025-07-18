package cn.icframework.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hzl
 * @since 2025/7/5
 */
@Configuration
@ComponentScan(basePackages = "cn.icframework.system")
@MapperScan({"cn.icframework.system.module.*.dao"})
public class SystemConfiguration {
}
