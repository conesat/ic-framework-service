package cn.icframework.hotel.module.room.pojo.vo;

import cn.icframework.mybatis.annotation.Join;
import cn.icframework.mybatis.annotation.Joins;
import cn.icframework.hotel.module.roompic.RoomPic;
import cn.icframework.hotel.module.roompic.pojo.vo.RoomPicSimpleVO;
import cn.icframework.hotel.module.roomsupporting.RoomSupporting;
import cn.icframework.hotel.module.supporting.Supporting;
import cn.icframework.hotel.module.supporting.pojo.vo.SupportingVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ic
 * @date 2024/09/25
 */
@Getter
@Setter
public class RoomForIndexVO extends RoomVO{
    /**
     * 配套设施
     */
    @Joins(joins = {
            @Join(joinTable = RoomSupporting.class, joinTableField = "roomId", selfField = "id"),
            @Join(joinTable = Supporting.class, joinTableField = "id", selfField = "supportingId")
    })
    private List<SupportingVO> supportingVOS;

    /**
     * 图片列表
     */
    @Joins(joins = {
            @Join(joinTable = RoomPic.class, joinTableField = "roomId", selfField = "id", where = "main_pic = 1"),
    })
    private RoomPicSimpleVO mainPic;
}
