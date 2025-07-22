package cn.icframework.system.module.user;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Index;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.common.enums.Sex;
import cn.icframework.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author hzl
 * @since 2023/5/31
 */
@Getter
@Setter
@Index(name = "user_idx", columns = {"username"}, unique = true)
@Table(value = "sys_user", comment = "系统用户表")
public class User {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 姓名
     */
    @TableField(length = 64, comment = "姓名")
    private String name;

    /**
     * 用户名
     */
    @TableField(comment = "用户名", length = 64, notNull = true)
    private String username;

    /**
     * 登录密码
     */
    @TableField(comment = "密码", length = 255, notNull = true)
    private String passwd;

    /**
     * 超级管理员，应该是判断该管理员是否具备超级管理员角色的，但是经常嵌套查询影响性能。这里多冗余多一个字段，在角色变更的时候进行调整
     */
    @TableField(defaultValue = "0", comment = "超级管理员")
    private Boolean su;

    @TableField(defaultValue = "1", comment = "状态")
    private Status status;

    @TableField(comment = "性别")
    private Sex sex;

    @TableField(length = 20, comment = "手机号")
    private String phone;

    @TableField(length = 100, comment = "邮箱")
    private String email;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 头像文件
     */
    @TableField(comment = "头像文件", length = 255)
    private Long avatarFileId;

    /**
     * 头像文件地址
     */
    @TableField(comment = "头像文件地址", length = 500)
    private String avatarFileUrl;

    /**
     * 更新时间
     */
    @TableField(comment = "更新时间", onUpdateValue = "now()")
    private LocalDateTime updateTime;
}
