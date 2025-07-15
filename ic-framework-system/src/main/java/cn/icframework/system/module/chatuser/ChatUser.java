package cn.icframework.system.module.chatuser;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.chat.Chat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "sys_chat_user", comment = "聊天用户关联")
public class ChatUser {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 聊天记录id
     */
    @ForeignKey(references = Chat.class)
    @TableField(value = "chat_id", notNull = true, comment = "聊天记录id")
    private Long chatId;

    /**
     * 用户id
     */
    @TableField(value = "user_id", notNull = true, comment = "用户id")
    private String userId;

    /**
     * 用户类型
     * @see cn.icframework.system.consts.UserType
     */
    @TableField(value = "user_type", notNull = true, comment = "用户类型")
    private String userType;

    /**
    * 加入聊天时间
    */
    @TableField(value = "create_time", notNull = true, comment = "加入聊天时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 已读时间
     */
    @TableField(value = "read_time", comment = "已读时间")
    private LocalDateTime readTime;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static ChatUser def() {
        ChatUser def = new ChatUser();
        return def;
    }
}
