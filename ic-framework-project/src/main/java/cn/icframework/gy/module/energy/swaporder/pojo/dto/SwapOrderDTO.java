package cn.icframework.project.module.energy.swaporder.pojo.dto;

import java.lang.Long;
import java.lang.Integer;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.lang.String;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2026/07/13
 */
@Getter
@Setter
public class SwapOrderDTO {
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 换电站ID
     */
    private Long stationId;
    /**
     * 电池ID
     */
    private Long batteryId;
    /**
     * 订单类型（1换电、2暂存、3租赁）
     */
    private Integer orderType;
    /**
     * 订单状态（1进行中、2完成、3异常、4取消）
     */
    private Integer status;
    /**
     * 实付金额
     */
    private BigDecimal amount;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    /**
     * 备注
     */
    private String remark;
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
