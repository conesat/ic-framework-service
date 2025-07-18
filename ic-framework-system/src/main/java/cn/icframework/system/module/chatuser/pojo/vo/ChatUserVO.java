package cn.icframework.system.module.chatuser.pojo.vo;

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
public class ChatUserVO {
    private Long id;
    /**
     * 聊天记录id
     */
    private Long chatId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userPic;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 加入聊天时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
