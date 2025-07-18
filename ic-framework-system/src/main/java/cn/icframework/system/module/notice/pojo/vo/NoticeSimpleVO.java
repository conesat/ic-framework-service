package cn.icframework.system.module.notice.pojo.vo;

import cn.icframework.common.enums.Status;
import cn.icframework.system.enums.NoticeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @since 2024/09/12
 */
@Getter
@Setter
public class NoticeSimpleVO {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 是与否有效
     */
    private Status status;
    /**
     * 类型
     */
    private NoticeType noticeType;
    /**
     * 归属人
     */
    private Long userId;
    /**
     * 归属人姓名
     */
    private String userName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 有效开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enableTime;
}
