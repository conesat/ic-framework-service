package cn.icframework.hotel.module.roomprice.pojo.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomPriceVO {
    private Long id;
    /**
     * 门市价
     */
    private Double price;
    /**
     * 日期
     */
    private LocalDate date;
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
