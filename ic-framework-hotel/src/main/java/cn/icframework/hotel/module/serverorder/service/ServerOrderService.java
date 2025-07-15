package cn.icframework.hotel.module.serverorder.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.common.enums.ServerStateEnum;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import cn.icframework.hotel.module.roomconsumeorder.dao.RoomConsumeOrderMapper;
import cn.icframework.hotel.module.roomconsumeorder.def.RoomConsumeOrderDef;
import cn.icframework.hotel.module.scheduling.def.SchedulingDef;
import cn.icframework.hotel.module.serverorder.ServerOrder;
import cn.icframework.hotel.module.serverorder.dao.ServerOrderMapper;
import cn.icframework.hotel.module.serverorder.def.ServerOrderDef;
import cn.icframework.hotel.module.serverorder.pojo.dto.ServerOrderDTO;
import cn.icframework.hotel.module.serverorder.wrapperbuilder.ServerOrderWrapperBuilder;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static cn.icframework.mybatis.wrapper.FunctionWrapper.COUNT;

/**
 * @author ic
 * @date 2024/11/29
 */
@Service
@RequiredArgsConstructor
public class ServerOrderService extends BasicService<ServerOrderMapper, ServerOrder> {
    private final RoomConsumeOrderMapper roomConsumeOrderMapper;
    private final ServerOrderWrapperBuilder wrapperBuilder;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(ServerOrderDTO dto) {
        ServerOrder entity = dto.getId() != null ? selectById(dto.getId()) : ServerOrder.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity, true);
        } else {
            insert(entity);
        }
    }

    /**
     * 自动分配订单给服务员
     *
     * @param serverTargetEnum
     * @param id
     * @param content
     * @param doTime
     */
    public void autoCreate(ServerTargetEnum serverTargetEnum, Long id, String content, LocalDateTime doTime) {
        // 找出值班人员并且按接单数排序，优先分给接单少的
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        ServerOrderDef serverOrderDef = ServerOrderDef.table();
        SchedulingDef schedulingDef = SchedulingDef.table();
        SqlWrapper sqlWrapper = SELECT(schedulingDef.userId)
                .FROM(schedulingDef)
                .LEFT_JOIN(serverOrderDef).ON(schedulingDef.userId.eq(serverOrderDef.serverUserId))
                .WHERE(schedulingDef.date.eq(nowDate)
                        .signInTime.notNull()
                        .signInTime.le(nowTime)
                        .signOutTime.isNull())
                .GROUP_BY(schedulingDef.userId)
                .ORDER_BY_ASC(COUNT(serverOrderDef.id))
                .LIMIT(1);
        Long serverUserId = selectOne(sqlWrapper, Long.class);
        ServerOrder serverOrder = ServerOrder.def();
        if (serverUserId != null) {
            serverOrder.setServerUserId(serverUserId);
        }
        if (serverOrder.getServerUserId() != null) {
            serverOrder.setState(ServerStateEnum.WAIT_START);
        } else {
            serverOrder.setState(ServerStateEnum.WAIT);
        }
        serverOrder.setTarget(serverTargetEnum);
        serverOrder.setTargetId(id);
        serverOrder.setDoTime(doTime);
        serverOrder.setTargetContent(content);
        insert(serverOrder);
    }

    public void assign(Long userId, Long[] ids) {
        ServerOrderDef serverOrderDef = ServerOrderDef.table();
        update(UPDATE(serverOrderDef)
                .SET(serverOrderDef.serverUserId, userId)
                .SET(serverOrderDef.state, ServerStateEnum.WAIT_START.code())
                .WHERE(serverOrderDef.id.in(ids)));
    }


    @Override
    public void after(SqlCommandType sqlCommandType, ServerOrder entity) {
        super.after(sqlCommandType, entity);
        if (sqlCommandType == SqlCommandType.DELETE) {
            if (entity.getTarget() == ServerTargetEnum.SALE) {
                RoomConsumeOrderDef roomConsumeOrderDef = RoomConsumeOrderDef.table();
                roomConsumeOrderMapper.delete(DELETE().FROM(roomConsumeOrderDef).WHERE(roomConsumeOrderDef.id.eq(entity.getTargetId())));
            }
        }
    }

}
