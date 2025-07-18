package cn.icframework.system.module.chatmsg.pojo.vo;

import cn.icframework.system.enums.MsgType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @since 2025/01/17
 */
@Getter
@Setter
public class ChatMsgVO {
    private Long id;
    /**
     * 发送者
     */
    private String userId;
    private String userName;
    private String userPic;
    /**
     * 聊天记录id
     */
    private String chatId;
    /**
     * 内容
     */
    private String message;
    /**
     * 概要
     */
    private String summary;
    /**
     * 类型
     */
    private MsgType msgType;
    /**
     * 发送用户类型
     */
    private String userType;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
