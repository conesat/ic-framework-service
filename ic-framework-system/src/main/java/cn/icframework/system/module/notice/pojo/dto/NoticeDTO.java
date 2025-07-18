package cn.icframework.system.module.notice.pojo.dto;

import cn.icframework.common.enums.Status;
import cn.icframework.system.enums.NoticeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @since 2024/09/12
 */
@Getter
@Setter
public class NoticeDTO {
    private Long id;
    /**
     * 标题
     */
    @NotEmpty
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 是与否有效
     */
    @NotNull
    private Status status;
    /**
     * 类型
     */
    @NotNull
    private NoticeType noticeType;
    /**
     * 归属人
     */
    private Long userId;
    /**
     * 定向用户
     */
    private Long[] receiveUserIds;
    /**
     * 定向岗位
     */
    private Long[] receiveDepIds;
    /**
     * 定向部门
     */
    private Long[] receivePosIds;
    /**
     * 定向角色
     */
    private Long[] receiveRoleIds;

    /**
     * 有效开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enableTime;
}
