package cn.icframework.project.controller;

import cn.icframework.project.util.VersionReader;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本信息控制器
 * 提供版本信息的API接口
 */
@RestController
@RequestMapping("/api/version")
public class VersionController {
    
    @Autowired
    private VersionReader versionReader;
    
    /**
     * 获取版本信息
     * @return 版本信息JSON
     */
    @GetMapping
    public JSONObject getVersion() {
        return versionReader.readVersion();
    }
    
    /**
     * 获取版本号
     * @return 版本号
     */
    @GetMapping("/version")
    public String getVersionNumber() {
        return versionReader.getVersion();
    }
    
    /**
     * 获取构建时间戳
     * @return 构建时间戳
     */
    @GetMapping("/timestamp")
    public String getTimestamp() {
        return versionReader.getTimestamp();
    }
} 