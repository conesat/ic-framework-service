package cn.icframework.system.module.noticereceiver.pojo.dto;

import java.lang.Long;
import java.lang.Boolean;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/13
 */
@Getter
@Setter
public class NoticeReceiverDTO {
    private Long id;
    /**
     * 通知id
     */
    private Long noticeId;
    /**
     * 用户id
     */
    private Long userId;
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
