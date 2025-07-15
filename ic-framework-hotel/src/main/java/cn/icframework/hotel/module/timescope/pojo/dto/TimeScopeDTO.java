package cn.icframework.hotel.module.timescope.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * @author ic
 * @date 2024/11/26
 */
@Getter
@Setter
public class TimeScopeDTO {
    private Long id;
    /**
     * 所属部门
     */
    private Long deptId;
    /**
     * 名称
     */
    private String name;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

}
