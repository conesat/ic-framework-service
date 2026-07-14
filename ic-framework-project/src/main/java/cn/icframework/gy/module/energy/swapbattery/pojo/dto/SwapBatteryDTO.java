package cn.icframework.project.module.energy.swapbattery.pojo.dto;

import java.lang.Long;
import java.lang.Integer;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.lang.String;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2026/07/13
 */
@Getter
@Setter
public class SwapBatteryDTO {
    private Long id;
    /**
     * 电池编号
     */
    private String batteryNo;
    /**
     * 所属换电站
     */
    private Long stationId;
    /**
     * 电池型号
     */
    private String model;
    /**
     * 电压（V）
     */
    private Integer voltage;
    /**
     * 容量（Ah）
     */
    private Integer capacity;
    /**
     * 当前电量（%）
     */
    private Integer powerPercent;
    /**
     * 健康度（%）
     */
    private Integer healthPercent;
    /**
     * 状态（1可租、2使用中、3暂存、4维修、0停用）
     */
    private Integer status;
    /**
     * 最近换电时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSwapTime;
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

}
