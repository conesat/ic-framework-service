package com.ic.framework.aot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Hello 控制器测试类
 * 
 * @author IC Framework
 * @version 1.0.0
 */
@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("IC Framework Spring AOT"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, World!"))
                .andExpect(jsonPath("$.service").value("IC Framework Spring AOT"));
    }

    @Test
    void testHelloEndpointWithName() throws Exception {
        mockMvc.perform(get("/api/hello").param("name", "IC Framework"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, IC Framework!"))
                .andExpect(jsonPath("$.service").value("IC Framework Spring AOT"));
    }

    @Test
    void testInfoEndpoint() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['java.version']").exists())
                .andExpect(jsonPath("$['os.name']").exists());
    }
} 