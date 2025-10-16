package cn.icframework.system.module.user.pojo.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginInfo {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户ID
     */
    private Object id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 头像文件URL
     */
    private String avatarFileUrl;
}
