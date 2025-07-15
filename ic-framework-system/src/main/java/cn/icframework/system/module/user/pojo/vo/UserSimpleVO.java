package cn.icframework.system.module.user.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hzl
 * @date 2023/5/31
 */
@Getter
@Setter
public class UserSimpleVO {

    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarFileUrl;
}
