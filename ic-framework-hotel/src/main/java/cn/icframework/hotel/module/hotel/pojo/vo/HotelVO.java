package cn.icframework.hotel.module.hotel.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/09/24
 */
@Getter
@Setter
public class HotelVO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 星级
     */
    private Integer starLevel;
    /**
     * 电话
     */
    private String tel;
    /**
     * 封面文件id
     */
    private Long picFileId;
    private String picFileUrl;
    /**
     * 酒店经理
     */
    private String managerUserId;
    private String managerUserName;
    /**
     * 地址
     */
    private String address;
    /**
     * 经度
     */
    private Double latitude;
    /**
     * 纬度
     */
    private Double longitude;
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
