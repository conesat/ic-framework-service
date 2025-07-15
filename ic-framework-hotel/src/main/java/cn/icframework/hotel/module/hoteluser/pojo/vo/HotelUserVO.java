package cn.icframework.hotel.module.hoteluser.pojo.vo;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import cn.icframework.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/25
 */
@Getter
@Setter
public class HotelUserVO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像文件
     */
    private Long avatarFileId;
    /**
     * 头像文件地址
     */
    private String avatarFileUrl;
    /**
     * 是否vip
     */
    private Boolean vip;
    /**
     * 是否有效
     */
    private Status status;
    /**
     * vip开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime vipStartTime;
    /**
     * vip到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime vipEndTime;
    /**
     * 累计消费金额
     */
    private BigDecimal totalConsume;
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
