package cn.icframework.hotel.module.roomtype.pojo.dto;

import java.lang.Integer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class RoomTypeDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 标准人数
     */
    private Integer peopleCount;

}
