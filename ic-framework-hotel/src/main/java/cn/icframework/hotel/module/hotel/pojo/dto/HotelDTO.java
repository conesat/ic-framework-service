package cn.icframework.hotel.module.hotel.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/24
 */
@Getter
@Setter
public class HotelDTO {
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
    /**
     * 酒店经理
     */
    private String managerUserId;
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
}
