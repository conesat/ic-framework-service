package cn.icframework.system.module.chatmsg;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.enums.MsgType;
import cn.icframework.system.module.chat.Chat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "sys_chat_msg", comment = "消息")
public class ChatMsg {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 发送者
     */
    @TableField(notNull = true, comment = "发送者")
    private String userId;

    /**
     * 聊天记录id
     */
    @ForeignKey(references = Chat.class)
    @TableField(notNull = true, comment = "聊天记录id")
    private Long chatId;

    /**
     * 内容
     */
    @TableField(notNull = true, length = 500, comment = "内容")
    private String message;

    /**
     * 概要
     */
    @TableField(notNull = true, length = 30, comment = "概要")
    private String summary;

    /**
     * 类型
     */
    @TableField(length = 4, comment = "类型")
    private MsgType msgType;

    /**
     * 发送用户类型
     */
    @TableField(length = 20, comment = "发送用户类型")
    private String userType;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
