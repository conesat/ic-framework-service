package cn.icframework.system.module.userpos;


import cn.icframework.mybatis.annotation.*;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.position.Position;
import cn.icframework.system.module.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Index(name = "user_position_idx", columns = {"user_id", "position_id"}, unique = true)
@Table(value = "sys_user_pos", comment = "用户岗位关联")
public class UserPos {
    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    @ForeignKey(references = Position.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "职位id")
    private Long positionId;

    @ForeignKey(references = User.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(notNull = true, comment = "用户id")
    private Long userId;

    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
