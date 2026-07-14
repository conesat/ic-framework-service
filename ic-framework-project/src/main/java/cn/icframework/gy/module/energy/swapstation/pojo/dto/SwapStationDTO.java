package cn.icframework.project.module.energy.swapstation.pojo.dto;

import java.lang.Long;
import java.lang.Integer;
import java.time.LocalDateTime;
import java.lang.Double;
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
public class SwapStationDTO {
    private Long id;
    /**
     * 站点编号
     */
    private String stationCode;
    /**
     * 站点名称
     */
    private String name;
    /**
     * 城市
     */
    private String city;
    /**
     * 区域
     */
    private String district;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 营业时间
     */
    private String businessHours;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 营业状态（1营业中、0停用、2维护）
     */
    private Integer status;
    /**
     * 电柜总格口数
     */
    private Integer totalCabinets;
    /**
     * 可用电池数
     */
    private Integer availableBatteryCount;
    /**
     * 可还空位数
     */
    private Integer availableReturnSlots;
    /**
     * 评分
     */
    private Double rating;
    /**
     * 服务设施
     */
    private String facilities;
    /**
     * 站点公告
     */
    private String announcement;
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
