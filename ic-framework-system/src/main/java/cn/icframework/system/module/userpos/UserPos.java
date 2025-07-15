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
@Table("sys_user_pos")
public class UserPos {
    @Id(idType = IdType.SNOWFLAKE)
    @TableField
    private Long id;

    @ForeignKey(references = Position.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "position_id", notNull = true, comment = "职位id")
    private Long positionId;

    @ForeignKey(references = User.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "user_id", notNull = true, comment = "用户id")
    private Long userId;

    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    public static UserPos def() {
        return new UserPos();
    }
}
