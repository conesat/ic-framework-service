package cn.icframework.hotel.module.scheduling.pojo.vo;

import cn.icframework.hotel.common.enums.SignStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author ic
 * @date 2024/11/26
 */
@Getter
@Setter
public class SchedulingVO {
    private Long id;
    /**
     * 所属用户
     */
    private Long userId;
    /**
     * 班次
     */
    private Long timeScopeId;
    private LocalTime timeScopeStartTime;
    private LocalTime timeScopeEndTime;
    private String timeScopeName;
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    /**
     * 签到时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime signInTime;

    /**
     * 签退时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime signOutTime;

    /**
     * 出席状态
     */
    private SignStatusEnum signStatus = SignStatusEnum.NOT_CLOCKED_IN;

    public SignStatusEnum getSignStatus() {
        if (!LocalDate.now().isBefore(date)) {
            if (LocalDate.now().isEqual(date) && LocalTime.now().isBefore(timeScopeEndTime)) {
                // 当天且未下班
                if (LocalTime.now().isAfter(timeScopeStartTime)) {
                    if (signInTime == null) {
                        return SignStatusEnum.LATE;
                    } else {
                        return SignStatusEnum.ATTENDED;
                    }
                }
            } else {
                // 已下班
                if (signInTime == null && signOutTime == null) {
                    return SignStatusEnum.ABSENT;
                } else if (signInTime != null && signOutTime != null) {
                    if (signInTime.isAfter(timeScopeStartTime) && signOutTime.isBefore(timeScopeEndTime)) {
                        // 迟到早退
                        return SignStatusEnum.LATE_EARLY_LEAVE;
                    } else if (signInTime.isBefore(timeScopeStartTime)) {
                        // 迟到
                        return SignStatusEnum.LATE;
                    } else if (signOutTime.isBefore(timeScopeEndTime)) {
                        // 早退
                        return SignStatusEnum.EARLY_LEAVE;
                    }
                    return SignStatusEnum.ATTENDED;
                } else {
                    return SignStatusEnum.MISSING;
                }
            }
        }
        return SignStatusEnum.NOT_CLOCKED_IN;
    }

}
