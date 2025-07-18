package cn.icframework.system.module.depuser;

import cn.icframework.mybatis.annotation.*;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.dep.Dept;
import cn.icframework.system.module.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Index(name = "dep_user_idx", columns = {"dep_id", "user_id"}, unique = true)
@Table(value = "sys_dep_user", comment = "部门用户")
public class DepUser {
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 部门id
     */
    @ForeignKey(references = Dept.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "部门id")
    private Long depId;

    /**
     * 用户id
     */
    @ForeignKey(references = User.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "用户id")
    private Long userId;

    /**
     * 是否负责人
     */
    @TableField(notNull = true, defaultValue = "false", comment = "是否负责人")
    private Boolean manager;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
