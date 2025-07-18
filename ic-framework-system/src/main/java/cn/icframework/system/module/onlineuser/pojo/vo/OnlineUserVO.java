package cn.icframework.system.module.onlineuser.pojo.vo;

import cn.icframework.system.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @since 2024/09/11
 */
@Getter
@Setter
public class OnlineUserVO {
    private Long sessionId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户姓名
     */
    private String name;
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
    private String userTypeText;
    private String avatarFileUrl;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;
    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    public String getUserTypeText() {
        return UserType.instanceOf(userType).text();
    }
}
