package cn.icframework.system.module.onlineuser.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/11
 */
@Getter
@Setter
public class OnlineUserDTO {
    private Long sessionId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 登录ip
     */
    private String ip;
    /**
     * 登录地址
     */
    private String location;
    /**
     * 操作系统
     */
    private String system;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 平台
     */
    private String platform;
    /**
     * 用户类型
     */
    private String userType;

}
