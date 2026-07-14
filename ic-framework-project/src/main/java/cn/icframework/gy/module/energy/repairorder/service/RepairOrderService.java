package cn.icframework.project.module.energy.repairorder.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.project.module.energy.repairorder.RepairOrder;
import cn.icframework.project.module.energy.repairorder.dao.RepairOrderMapper;
import cn.icframework.project.module.energy.repairorder.pojo.dto.RepairOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** 维修工单服务。 */
@Service
@RequiredArgsConstructor
public class RepairOrderService extends BasicService<RepairOrderMapper, RepairOrder> {

    /** 编辑或者保存。 */
    @Transactional
    public void edit(RepairOrderDTO dto) {
        RepairOrder entity = dto.getId() != null ? selectById(dto.getId()) : new RepairOrder();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    /** 创建用户提交的维修工单，用户与初始状态由服务端托管。 */
    @Transactional
    public RepairOrder createForUser(Long userId, Long stationId, String repairType, String description,
                                     String images, String contactPhone) {
        Assert.isNotNull(userId, "登录状态已失效");
        Assert.isNotEmpty(repairType, "请选择故障类型");
        Assert.isNotEmpty(description, "请填写故障描述");

        RepairOrder order = RepairOrder.def();
        order.setOrderNo("RP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase());
        order.setUserId(userId);
        order.setStationId(stationId);
        order.setRepairType(repairType);
        order.setDescription(description);
        order.setImages(images);
        order.setContactPhone(contactPhone);
        order.setStatus(1);
        insert(order);
        return order;
    }

    /** 用户只能取消自己的待处理工单。 */
    @Transactional
    public void cancelForUser(Long userId, Long orderId) {
        RepairOrder order = selectById(orderId);
        Assert.isNotNull(order, "工单不存在");
        Assert.isTrue(userId != null && userId.equals(order.getUserId()), "无权操作该工单");
        Assert.isTrue(Integer.valueOf(1).equals(order.getStatus()), "当前工单不可取消");
        order.setStatus(4);
        updateById(order);
    }
}
