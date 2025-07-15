package cn.icframework.hotel.module.roomsupporting.pojo.dto;


import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/08
 */
@Getter
@Setter
public class RoomSupportingDTO {
    private Long id;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 配套id
     */
    private Long supportingId;

}
