package cn.icframework.hotel.module.serverorder.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import cn.icframework.hotel.module.room.Room;
import cn.icframework.hotel.module.room.dao.RoomMapper;
import cn.icframework.hotel.module.room.def.RoomDef;
import cn.icframework.hotel.module.roomconsumeorder.def.RoomConsumeOrderDef;
import cn.icframework.hotel.module.roomorder.dao.RoomOrderMapper;
import cn.icframework.hotel.module.roomorder.def.RoomOrderDef;
import cn.icframework.hotel.module.serverorder.ServerOrder;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static cn.icframework.mybatis.wrapper.Wrapper.SELECT;

/**
 * @author ic
 * @date 2024/11/29
 */
@AllArgsConstructor
@Component
public class ServerOrderVOConverter extends BasicConverter<ServerOrder, ServerOrderVO> {
    private final RoomOrderMapper roomOrderMapper;
    private final RoomMapper roomMapper;
    @Override
    protected void convert(ServerOrderVO vo, ServerOrder entity) {
        if (entity != null) {
            if (vo.getTarget().equals(ServerTargetEnum.SALE)) {
                RoomDef roomDef = RoomDef.table();
                RoomOrderDef roomOrderDef = RoomOrderDef.table();
                RoomConsumeOrderDef roomConsumeOrderDef = RoomConsumeOrderDef.table();
                SqlWrapper sqlWrapper = SELECT(roomOrderDef.id.as(ServerOrderVO::getRoomOrderId),
                        roomDef.id.as(ServerOrderVO::getRoomId),
                        roomDef.no.as(ServerOrderVO::getRoomNo))
                        .FROM(roomConsumeOrderDef)
                        .INNER_JOIN(roomOrderDef).ON(roomOrderDef.id.eq(roomConsumeOrderDef.roomOrderId))
                        .INNER_JOIN(roomDef).ON(roomOrderDef.roomId.eq(roomDef.id))
                        .WHERE(roomConsumeOrderDef.id.eq(vo.getTargetId()));
                ServerOrderVO orderWithRoomOrderVO = roomOrderMapper.selectOneAs(sqlWrapper, ServerOrderVO.class);
                vo.setRoomOrderId(orderWithRoomOrderVO.getRoomOrderId());
                vo.setRoomId(orderWithRoomOrderVO.getRoomId());
                vo.setRoomNo(orderWithRoomOrderVO.getRoomNo());
            } else if (vo.getTarget().equals(ServerTargetEnum.CLEAR)) {
                Room room = roomMapper.selectById(vo.getTargetId());
                vo.setRoomId(room.getId());
                vo.setRoomNo(room.getNo());
            }
        }
    }
}
