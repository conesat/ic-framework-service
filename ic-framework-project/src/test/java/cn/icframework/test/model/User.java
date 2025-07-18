package cn.icframework.test.model;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hzl
 * @since 2025/7/11
 */
@Getter
@Setter
@Table(value = "test_user")
public class User {
    @Id
    @TableField
    private Long id;
    @TableField
    private String username;
    @TableField
    private String password;
    @TableField
    private String nickname;
    @TableField
    private String avatar;
}
