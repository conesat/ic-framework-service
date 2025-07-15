package cn.icframework.hotel.module.floor.pojo.vo;

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
public class FloorVO {
    private Long id;
    /**
     * 外部key
     */
    private String extKey;
    /**
     * 名称
     */
    private String name;
    /**
     * 楼层
     */
    private Integer level;
    /**
     * 所属楼栋
     */
    private Long buildingId;
    private String buildingName;
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
