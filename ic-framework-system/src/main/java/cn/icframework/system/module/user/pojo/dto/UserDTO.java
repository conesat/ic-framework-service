package cn.icframework.system.module.user.pojo.dto;

import cn.icframework.common.enums.Sex;
import cn.icframework.common.enums.Status;
import cn.icframework.system.consts.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author hzl
 * @since 2023/5/31
 */
@Getter
@Setter
public class UserDTO {

    private Long id;

    /**
     * 姓名
     */
    @NotEmpty
    private String name;

    /**
     * 用户名
     */
    @NotEmpty
    private String username;

    /**
     * 状态
     */
    @NotNull
    private Status status;

    /**
     * 登录密码
     */
    private String passwd;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户类型
     */
    private String userType = UserType.SYSTEM_USER;

    /**
     * 性别
     */
    private Sex sex;

    /**
     * 角色
     */
    private Long[] roleIds;

    /**
     * 所属部门
     */
    private Long[] depIds;

    /**
     * 岗位
     */
    private Long[] posIds;

    /**
     * 头像文件
     */
    private Long avatarFileId;

    public Long[] getRoleIds() {
        return roleIds == null ? new Long[0] : roleIds;
    }
}
