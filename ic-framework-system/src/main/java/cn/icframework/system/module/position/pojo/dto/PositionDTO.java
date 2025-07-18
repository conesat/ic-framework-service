package cn.icframework.system.module.position.pojo.dto;

import java.lang.Long;
import java.lang.Integer;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.lang.String;
import cn.icframework.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2024/09/09
 */
@Getter
@Setter
public class PositionDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String sign;
    /**
     * 职级
     */
    private Integer level;
    /**
     * 是与否有效
     */
    private Status status;
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
