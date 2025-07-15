package cn.icframework.hotel.module.room.pojo.vo;

import cn.icframework.hotel.common.enums.RoomStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomStaticsVO {
    private RoomStatusEnum status;
    private long count;
}
