package cn.icframework.system.module.chat.pojo.dto;

import cn.icframework.system.enums.ChatType;
import cn.icframework.system.enums.MsgType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2025/01/17
 */
@Getter
@Setter
public class ChatDTO {
    private Long id;
    /**
     * 头像文件
     */
    private Long avatarFileId;
    /**
     * 内容
     */
    private String message;
    /**
     * 名称
     */
    private String name;
    /**
     * 概要
     */
    private String summary;
    /**
     * 类型
     */
    private MsgType msgType;
    /**
     * 聊天类型
     */
    private ChatType chatType;
    /**
     * 是否临时
     */
    private Boolean temporary;

}
