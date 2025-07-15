package cn.icframework.hotel.module.roomconsumeorder.pojo.vo;

import cn.icframework.mybatis.annotation.Join;
import cn.icframework.mybatis.annotation.Joins;
import cn.icframework.hotel.common.enums.RoomConsumeOrderStatus;
import cn.icframework.hotel.module.room.Room;
import cn.icframework.hotel.module.room.pojo.vo.RoomVO;
import cn.icframework.hotel.module.roomorder.RoomOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class RoomConsumeOrderVO {
    private Long id;
    /**
     * 所属订单
     */
    private Long roomOrderId;

    @Joins(joins = {
            @Join(joinTable = RoomOrder.class, joinTableField = "id", selfField = "roomOrderId"),
            @Join(joinTable = Room.class, joinTableField = "id", selfField = "roomId")
    })
    private RoomVO roomVO;

    /**
     * 项目id
     */
    private Long customerItemId;
    private String customerItemName;
    /**
     * 金额
     */
    private Double price;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 状态
     */
    private RoomConsumeOrderStatus status;
    /**
     * 图片
     */
    private String fileUrl;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
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
