package cn.icframework.hotel.module.building.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class BuildingVO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 所属酒店
     */
    private Long hotelId;
    private String hotelName;
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
