package cn.icframework.hotel.module.timescope.pojo.vo;

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
public class TimeScopeVO {
    private Long id;
    /**
     * 所属部门
     */
    private Long deptId;

    /**
     * 所属部门
     */
    private String deptName;
    /**
     * 名称
     */
    private String name;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

}
