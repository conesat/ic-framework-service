package cn.icframework.hotel.module.room.pojo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomUpdateStatusDTO extends RoomUpdateDTO {
    /**
     * 保洁人员id
     */
    private Long clearUserId;
    /**
     * 是否需要清洁
     */
    private Boolean needClear;
}
