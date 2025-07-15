package cn.icframework.hotel.module.floor.pojo.dto;

import java.lang.Integer;
import java.time.LocalDateTime;

import cn.icframework.mybatis.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class FloorDTO {
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
