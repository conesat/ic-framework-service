package cn.icframework.system.module.chatmsg.pojo.dto;

import cn.icframework.system.enums.MsgType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2025/01/17
 */
@Getter
@Setter
public class ChatMsgDTO {
    private Long id;
    /**
     * 发送者
     */
    private String userId;
    /**
     * 接收者
     */
    private Long chatId;
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
}
