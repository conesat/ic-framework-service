package cn.icframework.hotel.module.scheduling.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author ic
 * @date 2024/11/26
 */
@Getter
@Setter
public class SchedulingDTO {
    private Long id;
    /**
     * 所属用户
     */
    private List<Long> userIds;
    /**
     * 班次
     */
    private Long timeScopeId;
    /**
     * 日期
     */
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDate date;
    /**
     * 日期
     */
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDate dateStart;
    /**
     * 日期
     */
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDate dateEnd;

    /**
     * 签到时间
     */
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalTime signInTime;

    /**
     * 签退时间
     */
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalTime signOutTime;
}
