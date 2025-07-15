package cn.icframework.hotel.module.roomsupporting.pojo.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/08
 */
@Getter
@Setter
public class RoomSupportingVO {
    private Long id;
    /**
     * 房间id
     */
    private Long roomId;
    private String roomNo;
    /**
     * 配套id
     */
    private Long supportingId;
    private String supportingName;
    /**
     * 配套设施图标
     */
    private String iconFileUrl;

}
