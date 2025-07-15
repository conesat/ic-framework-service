package cn.icframework.hotel.module.scheduling.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author ic
 * @date 2024/11/26
 */
@Getter
@Setter
public class SchedulingQueryDTO {
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 岗位id
     */
    private Long posId;
    /**
     * 班次
     */
    private Long timeScopeId;
    /**
     * 日期
     */
    @NotNull
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDate dateStart;
    /**
     * 日期
     */
    @NotNull
    @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDate dateEnd;
    /**
     * 是否签到
     */
    private Boolean sign;

}
