package com.ic.framework.aot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 示例 REST 控制器
 * 
 * @author IC Framework
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "IC Framework Spring AOT");
        response.put("version", "1.0.0");
        return response;
    }

    /**
     * 问候接口
     */
    @GetMapping("/hello")
    public Map<String, Object> hello(@RequestParam(defaultValue = "World") String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello, " + name + "!");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "IC Framework Spring AOT");
        return response;
    }

    /**
     * 系统信息接口
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("java.version", System.getProperty("java.version"));
        response.put("java.vendor", System.getProperty("java.vendor"));
        response.put("os.name", System.getProperty("os.name"));
        response.put("os.version", System.getProperty("os.version"));
        response.put("user.timezone", System.getProperty("user.timezone"));
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
} 