package cn.icframework.hotel.module.building.pojo.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class BuildingDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 所属酒店
     */
    private Long hotelId;
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
