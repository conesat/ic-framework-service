package cn.icframework.system.module.noticereceiver.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @since 2024/09/13
 */
@Getter
@Setter
public class NoticeReceiverVO {
    private Long id;
    /**
     * 通知id
     */
    private Long noticeId;
    /**
     * 用户id
     */
    private Long userId;
    private String userName;
    /**
     * 头像
     */
    private String avatarFileUrl;
    /**
     * 隐藏，不在列表显示
     */
    private Boolean hidden;
    /**
     * 查阅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;

}
