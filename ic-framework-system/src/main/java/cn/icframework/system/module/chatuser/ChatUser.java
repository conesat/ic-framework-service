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
    @TableField(notNull = true, comment = "聊天记录id")
    private Long chatId;

    /**
     * 用户id
     */
    @TableField(notNull = true, comment = "用户id")
    private String userId;

    /**
     * 加入聊天时间
     */
    @TableField(notNull = true, comment = "加入聊天时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 已读时间
     */
    @TableField(comment = "已读时间")
    private LocalDateTime readTime;
}
