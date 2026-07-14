package cn.icframework.project.module.energy.swaporder.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.project.module.energy.swapbattery.SwapBattery;
import cn.icframework.project.module.energy.swapbattery.service.SwapBatteryService;
import cn.icframework.project.module.energy.swapbattery.wrapperbuilder.SwapBatteryWrapperBuilder;
import cn.icframework.project.module.energy.swaporder.SwapOrder;
import cn.icframework.project.module.energy.swaporder.dao.SwapOrderMapper;
import cn.icframework.project.module.energy.swaporder.pojo.dto.SwapOrderDTO;
import cn.icframework.project.module.energy.swapstation.SwapStation;
import cn.icframework.project.module.energy.swapstation.service.SwapStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 换电订单服务。
 */
@Service
@RequiredArgsConstructor
public class SwapOrderService extends BasicService<SwapOrderMapper, SwapOrder> {
    public static final int TYPE_SWAP = 1;
    public static final int TYPE_STORAGE = 2;
    public static final int TYPE_RENTAL = 3;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_CANCELLED = 4;

    private final SwapStationService swapStationService;
    private final SwapBatteryService swapBatteryService;
    private final SwapBatteryWrapperBuilder swapBatteryWrapperBuilder;

    /** 编辑或者保存。 */
    @Transactional
    public void edit(SwapOrderDTO dto) {
        SwapOrder entity = dto.getId() != null ? selectById(dto.getId()) : new SwapOrder();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    /**
     * 为当前用户创建换电、暂存或租赁订单，并同步锁定库存资源。
     */
    @Transactional
    public SwapOrder createForUser(Long userId, Long stationId, Integer orderType, String remark) {
        Assert.isNotNull(userId, "登录状态已失效");
        Assert.isNotNull(stationId, "请选择换电站");
        Assert.isTrue(orderType != null && List.of(TYPE_SWAP, TYPE_STORAGE, TYPE_RENTAL).contains(orderType), "不支持的订单类型");

        SwapStation station = swapStationService.selectById(stationId);
        Assert.isNotNull(station, "换电站不存在");
        Assert.isTrue(Integer.valueOf(1).equals(station.getStatus()), "该换电站暂不可服务");

        Long batteryId = null;
        if (orderType == TYPE_STORAGE) {
            int returnSlots = safeNumber(station.getAvailableReturnSlots());
            Assert.isTrue(returnSlots > 0, "该站暂无可用暂存空位");
            station.setAvailableReturnSlots(returnSlots - 1);
            swapStationService.updateById(station);
        } else {
            QueryParams batteryParams = new QueryParams();
            batteryParams.put("stationId", stationId);
            batteryParams.put("status", 1);
            List<SwapBattery> batteries = swapBatteryService.select(swapBatteryWrapperBuilder.build(batteryParams));
            Assert.isTrue(!batteries.isEmpty(), "该站暂无可用电池");
            SwapBattery battery = batteries.get(0);
            battery.setStatus(2);
            battery.setLastSwapTime(LocalDateTime.now());
            swapBatteryService.updateById(battery);
            batteryId = battery.getId();
            station.setAvailableBatteryCount(Math.max(0, safeNumber(station.getAvailableBatteryCount()) - 1));
            swapStationService.updateById(station);
        }

        SwapOrder order = SwapOrder.def();
        order.setOrderNo(nextOrderNo(orderType));
        order.setUserId(userId);
        order.setStationId(stationId);
        order.setBatteryId(batteryId);
        order.setOrderType(orderType);
        order.setStatus(STATUS_IN_PROGRESS);
        order.setAmount(BigDecimal.ZERO);
        order.setStartTime(LocalDateTime.now());
        order.setRemark(remark);
        insert(order);
        return order;
    }

    /** 仅允许订单所属用户取消尚未完成的订单，并释放已锁定资源。 */
    @Transactional
    public void cancelForUser(Long userId, Long orderId) {
        SwapOrder order = selectById(orderId);
        Assert.isNotNull(order, "订单不存在");
        Assert.isTrue(userId != null && userId.equals(order.getUserId()), "无权操作该订单");
        Assert.isTrue(Integer.valueOf(STATUS_IN_PROGRESS).equals(order.getStatus()), "当前订单不可取消");

        SwapStation station = swapStationService.selectById(order.getStationId());
        if (station != null) {
            if (Integer.valueOf(TYPE_STORAGE).equals(order.getOrderType())) {
                station.setAvailableReturnSlots(safeNumber(station.getAvailableReturnSlots()) + 1);
            } else if (order.getBatteryId() != null) {
                SwapBattery battery = swapBatteryService.selectById(order.getBatteryId());
                if (battery != null && Integer.valueOf(2).equals(battery.getStatus())) {
                    battery.setStatus(1);
                    swapBatteryService.updateById(battery);
                }
                station.setAvailableBatteryCount(safeNumber(station.getAvailableBatteryCount()) + 1);
            }
            swapStationService.updateById(station);
        }
        order.setStatus(STATUS_CANCELLED);
        order.setEndTime(LocalDateTime.now());
        updateById(order);
    }

    private int safeNumber(Integer value) {
        return value == null ? 0 : value;
    }

    private String nextOrderNo(Integer orderType) {
        String prefix = switch (orderType) {
            case TYPE_STORAGE -> "ST";
            case TYPE_RENTAL -> "RT";
            default -> "SW";
        };
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
