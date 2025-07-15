package cn.icframework.system.module.onlineuser;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "sys_online_user", comment = "在线用户")
public class OnlineUser {

    @Id(idType = IdType.INPUT)
    private Long sessionId;

    /**
     * 用户id
     */
    @TableField(comment = "用户id")
    private Long userId;

    /**
     * 登录ip
     */
    @TableField(comment = "登录ip")
    private String ip;

    /**
     * 登录地址
     */
    @TableField(comment = "登录地址")
    private String location;

    /**
     * 操作系统
     */
    @TableField(comment = "操作系统")
    private String system;

    /**
     * 浏览器
     */
    @TableField(comment = "浏览器")
    private String browser;

    /**
     * 平台
     */
    @TableField(comment = "平台")
    private String platform;

    /**
     * 用户类型
     */
    @TableField(comment = "用户类型")
    private String userType;

    /**
     * 登录时间
     */
    @TableField(notNull = true, comment = "登录时间")
    private LocalDateTime loginTime;

    /**
     * 过期时间
     */
    @TableField(comment = "过期时间")
    private LocalDateTime expireTime;

    public static OnlineUser def() {
        return new OnlineUser();
    }
}
