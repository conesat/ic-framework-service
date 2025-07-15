package cn.icframework.hotel.module.roomconsumeorder.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.common.enums.RoomConsumeOrderStatus;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import cn.icframework.hotel.module.consumeitem.ConsumeItem;
import cn.icframework.hotel.module.consumeitem.service.ConsumeItemService;
import cn.icframework.hotel.module.roomconsumeorder.RoomConsumeOrder;
import cn.icframework.hotel.module.roomconsumeorder.dao.RoomConsumeOrderMapper;
import cn.icframework.hotel.module.roomconsumeorder.pojo.dto.RoomConsumeOrderBatchDTO;
import cn.icframework.hotel.module.roomconsumeorder.pojo.dto.RoomConsumeOrderBatchItemDTO;
import cn.icframework.hotel.module.roomconsumeorder.pojo.dto.RoomConsumeOrderDTO;
import cn.icframework.hotel.module.serverorder.dao.ServerOrderMapper;
import cn.icframework.hotel.module.serverorder.def.ServerOrderDef;
import cn.icframework.hotel.module.serverorder.service.ServerOrderService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ic
 * @date 2024/10/14
 */
@Service
@RequiredArgsConstructor
public class RoomConsumeOrderService extends BasicService<RoomConsumeOrderMapper, RoomConsumeOrder> {

    private final ConsumeItemService consumeItemService;
    private final ServerOrderService serverOrderService;
    private final ServerOrderMapper serverOrderMapper;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(RoomConsumeOrderDTO dto) {
        RoomConsumeOrder entity = dto.getId() != null ? selectById(dto.getId()) : RoomConsumeOrder.def();
        BeanUtils.copyExcludeProps(dto, entity);
        ConsumeItem consumeItem = consumeItemService.selectById(dto.getCustomerItemId());
        entity.setCustomerItemName(consumeItem.getName());
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    public void createBatch(RoomConsumeOrderBatchDTO form) {
        RoomConsumeOrderBatchItemDTO[] items = form.getItems();
        Set<Long> consumeOrderIds = Arrays.stream(items).map(RoomConsumeOrderBatchItemDTO::getCustomerItemId).collect(Collectors.toSet());
        List<ConsumeItem> consumeItems = consumeItemService.selectByIds(consumeOrderIds);
        Map<Long, ConsumeItem> consumeItemMap = consumeItems.stream().collect(Collectors.toMap(ConsumeItem::getId, item -> item));

        for (RoomConsumeOrderBatchItemDTO item : items) {
            ConsumeItem consumeItem = consumeItemMap.get(item.getCustomerItemId());
            Assert.isFalse(consumeItem.getInventory() != -1 && consumeItem.getInventory() < item.getNum(), "库存不足");
            RoomConsumeOrder roomConsumeOrder = RoomConsumeOrder.def();
            roomConsumeOrder.setStatus(RoomConsumeOrderStatus.PAYED);
            roomConsumeOrder.setPayTime(LocalDateTime.now());
            roomConsumeOrder.setRoomOrderId(form.getRoomOrderId());
            roomConsumeOrder.setPrice(item.getNum() * consumeItem.getPrice());
            roomConsumeOrder.setCustomerItemId(item.getCustomerItemId());
            roomConsumeOrder.setCustomerItemName(consumeItem.getName());
            roomConsumeOrder.setNum(item.getNum());
            if (consumeItem.getInventory() != -1) {
                consumeItem.setInventory(consumeItem.getInventory() - item.getNum());
                consumeItemService.updateById(consumeItem);
            }
            insert(roomConsumeOrder);
        }
    }

    @Override
    public void after(SqlCommandType sqlCommandType, RoomConsumeOrder entity) {
        super.after(sqlCommandType, entity);
        // 新建订单自动指派给服务员
        if (sqlCommandType == SqlCommandType.INSERT) {
            // 自动指派给服务员
            serverOrderService.autoCreate(ServerTargetEnum.SALE, entity.getId(), entity.getCustomerItemName() + "*" + entity.getNum(), null);
        } else if (sqlCommandType == SqlCommandType.DELETE) {
            ServerOrderDef serverOrderDef = ServerOrderDef.table();
            serverOrderMapper.delete(DELETE_FROM(serverOrderDef).WHERE(serverOrderDef.target.eq(ServerTargetEnum.SALE.code()).targetId.eq(entity.getId())));
        }
    }
}
