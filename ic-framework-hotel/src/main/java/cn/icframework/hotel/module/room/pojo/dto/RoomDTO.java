package cn.icframework.hotel.module.room.pojo.dto;

import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.hotel.common.enums.RoomStatusEnum;
import cn.icframework.hotel.common.enums.SaleStatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class RoomDTO {
    private Long id;
    /**
     * 优惠价
     */
    private Double preferentialPrice;

    /**
     * 门市价
     */
    private Double price;
    /**
     * 周末价
     */
    private Double weekPrice;
    /**
     * 房间号
     */
    private String no;
    /**
     * 是否在售
     */
    private SaleStatusEnum saleStatus;
    /**
     * 房间状态
     */
    private RoomStatusEnum roomStatus;
    /**
     * 所属楼层
     */
    private Long floorId;
    /**
     * 所属房型
     */
    private Long roomTypeId;
    /**
     * 扩展key
     */
    private String extKey;
    /**
     * 房间配套设施
     */
    private Long[] supportingIds;
    /**
     * 房间图片
     */
    private String picFiles;

}
