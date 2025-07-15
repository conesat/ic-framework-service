package cn.icframework.system.module.user.pojo.vo;

import cn.icframework.common.enums.Sex;
import cn.icframework.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author hzl
 * @date 2023/5/31
 */
@Getter
@Setter
public class UserVO extends UserSimpleVO {

    /**
     * 登录失败次数
     */
    private int loginFailCount;

    /**
     * 超级管理员
     */
    private Boolean su;

    /**
     * 状态
     */
    private Status status;

    /**
     * 性别
     */
    private Sex sex;

    /**
     * 头像文件id
     */
    private Long avatarFileId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;


    /**
     * 最后一次登陆失败时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginFailTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    public Sex getSex() {
        return sex == null ? Sex.UNKNOWN : sex;
    }
}
