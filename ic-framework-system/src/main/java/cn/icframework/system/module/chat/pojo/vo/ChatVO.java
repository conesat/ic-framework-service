package cn.icframework.system.module.chat.pojo.vo;

import cn.icframework.system.enums.ChatType;
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
public class ChatVO {
    private Long id;
    /**
     * 头像文件
     */
    private Long avatarFileId;
    /**
     * 未读消息
     */
    private Integer unreadCount;

    /**
     * 头像文件地址
     */
    private String avatarFileUrl;

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
    /**
     * 最新消息创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMsgTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
