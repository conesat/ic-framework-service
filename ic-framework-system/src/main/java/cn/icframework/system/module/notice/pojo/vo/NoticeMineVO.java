package cn.icframework.system.module.notice.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/09/12
 */
@Getter
@Setter
public class NoticeMineVO extends NoticeSimpleVO {
    /**
     * 已读时间，只有传userId才会有
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;
}
