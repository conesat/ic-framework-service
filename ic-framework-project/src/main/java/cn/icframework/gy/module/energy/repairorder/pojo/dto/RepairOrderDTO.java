package cn.icframework.project.module.energy.repairorder.pojo.dto;

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
public class RepairOrderDTO {
    private Long id;
    /**
     * 工单编号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 关联站点ID
     */
    private Long stationId;
    /**
     * 故障类型
     */
    private String repairType;
    /**
     * 问题描述
     */
    private String description;
    /**
     * 图片地址
     */
    private String images;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 状态（1待处理、2处理中、3已完成、4已取消）
     */
    private Integer status;
    /**
     * 处理人ID
     */
    private Long handlerId;
    /**
     * 处理说明
     */
    private String handleRemark;
    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime repairTime;
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
