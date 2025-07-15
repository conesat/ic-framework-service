package cn.icframework.hotel.module.room.pojo.vo;

import cn.icframework.hotel.common.enums.RoomStatusEnum;
import cn.icframework.hotel.common.enums.SaleStatusEnum;
import cn.icframework.hotel.module.roomorder.pojo.vo.RoomOrderSimpleVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomStatusItemVO {
    private boolean show;
    private int dayOfWeek;
    private String date;
    /**
     * 门市价
     */
    private Double price;
    /**
     * 是否在售
     */
    private SaleStatusEnum saleStatus;
    /**
     * 房间状态
     */
    private RoomStatusEnum roomStatus;

    /**
     * 订单
     */
    private RoomOrderSimpleVO order;
}
