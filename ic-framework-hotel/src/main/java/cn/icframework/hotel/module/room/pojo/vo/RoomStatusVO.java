package cn.icframework.hotel.module.room.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoomStatusVO {
    private RoomVO room;
    private List<RoomStatusItemVO> items = new ArrayList<>();
}
