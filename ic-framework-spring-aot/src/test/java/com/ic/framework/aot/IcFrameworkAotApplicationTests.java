package com.ic.framework.aot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * IC Framework Spring AOT 应用程序测试类
 * 
 * @author IC Framework
 * @version 1.0.0
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.aot.enabled=true"
})
class IcFrameworkAotApplicationTests {

    @Test
    void contextLoads() {
        // 测试 Spring 上下文是否正常加载
    }
} 