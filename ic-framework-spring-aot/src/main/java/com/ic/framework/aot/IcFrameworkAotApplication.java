package com.ic.framework.aot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IC Framework Spring AOT 应用程序主类
 * 
 * @author IC Framework
 * @version 1.0.0
 */
@SpringBootApplication(exclude = {
    org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration.class,
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
public class IcFrameworkAotApplication {

    public static void main(String[] args) {
        SpringApplication.run(IcFrameworkAotApplication.class, args);
    }
} 