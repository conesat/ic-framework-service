package cn.icframework.system.module.user.pojo.dto;

import cn.icframework.common.enums.Sex;
import cn.icframework.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author hzl
 * @date 2023/5/31
 */
@Getter
@Setter
public class UserMineDTO {

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
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Sex sex;

    /**
     * 头像文件
     */
    private Long avatarFileId;

}
