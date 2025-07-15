package cn.icframework.hotel.module.roomorder.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.core.utils.LocalDateTimeUtils;
import cn.icframework.hotel.common.enums.RoomStatusEnum;
import cn.icframework.hotel.module.room.Room;
import cn.icframework.hotel.module.room.dao.RoomMapper;
import cn.icframework.hotel.module.room.def.RoomDef;
import cn.icframework.hotel.module.roomorder.RoomOrder;
import cn.icframework.hotel.module.roomorder.dao.RoomOrderMapper;
import cn.icframework.hotel.module.roomorder.def.RoomOrderDef;
import cn.icframework.hotel.module.roomorder.pojo.dto.RoomOrderDTO;
import cn.icframework.hotel.module.roomorder.pojo.vo.RoomOrderVO;
import cn.icframework.hotel.module.roomprice.RoomPrice;
import cn.icframework.hotel.module.roomprice.def.RoomPriceDef;
import cn.icframework.hotel.module.roomprice.service.RoomPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ic
 * @date 2024/10/14
 */
@Service
@RequiredArgsConstructor
public class RoomOrderService extends BasicService<RoomOrderMapper, RoomOrder> {

    private final RoomPriceService roomPriceService;
    private final RoomMapper roomMapper;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(RoomOrderDTO dto) {
        RoomOrder entity = dto.getId() != null ? selectById(dto.getId()) : RoomOrder.def();
        BeanUtils.copyExcludeProps(dto, entity);
        Room room = roomMapper.selectById(entity.getRoomId());
        Assert.isNotNull(room, "房间不存在");
        long days = ChronoUnit.DAYS.between(dto.getInDate(), dto.getOutDate());
        List<Integer> dateWeek = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate localDate = dto.getInDate().plusDays(i);
            dates.add(LocalDateTimeUtils.getFormatDate(LocalDateTimeUtils.TIME_FORMAT_3STR, localDate));
            dateWeek.add(localDate.getDayOfWeek().getValue());
        }
        entity.setDay(dates.size());

        RoomPriceDef roomPriceDef = RoomPriceDef.table();
        List<RoomPrice> roomPriceList = roomPriceService.select(SELECT().FROM(roomPriceDef).WHERE(roomPriceDef.roomId.eq(entity.getRoomId()).date.in(dates)));
        Map<String, Double> roomDatePriceMap = roomPriceList.stream().collect(
                Collectors.toMap(f -> LocalDateTimeUtils.getFormatDate(LocalDateTimeUtils.TIME_FORMAT_3STR, f.getDate()), RoomPrice::getPrice)
        );
        entity.setRoomPrice(0D);
        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            Integer dayOfWeek = dateWeek.get(i);
            Double v = roomDatePriceMap.get(date);
            if (v != null) {
                entity.setRoomPrice(entity.getRoomPrice() + v);
            } else if ((dayOfWeek == 0 || dayOfWeek == 6) && room.getWeekPrice() != null) {
                entity.setRoomPrice(entity.getRoomPrice() + room.getWeekPrice());
            } else {
                entity.setRoomPrice(entity.getRoomPrice() + room.getPrice());
            }
        }
        if (dto.getPayed()) {
            entity.setActualRoomPrice(entity.getRoomPrice());
        }
        if (dto.getId() != null) {
            LocalDate localDate = LocalDate.now();
            RoomStatusEnum roomStatusEnum = entity.getCheckIn() && (localDate.isAfter(entity.getInDate()) || localDate.isEqual(entity.getInDate())) && localDate.isBefore(entity.getOutDate()) ?
                    RoomStatusEnum.CHECKED_IN : RoomStatusEnum.AWAIT_CHECK_IN;
            if (roomStatusEnum != room.getRoomStatus()) {
                RoomDef roomDef = RoomDef.table();
                roomMapper.updateWrapper(UPDATE(roomDef.roomStatus.set(roomStatusEnum.code())).WHERE(roomDef.id.eq(room.getId())));
            }
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    /**
     * 获取房间当前订单
     *
     * @param roomId
     * @return
     */
    public RoomOrderVO roomNowOrder(Serializable roomId) {
        RoomOrderDef roomOrderDef = RoomOrderDef.table();
        String date = LocalDateTimeUtils.getFormatDate(LocalDateTimeUtils.TIME_FORMAT_6STR);
        return selectOne(SELECT()
                .FROM(roomOrderDef)
                .WHERE(roomOrderDef.roomId.eq(roomId).inDate.le(date).outDate.ge(date))
                .ORDER_BY_DESC(roomOrderDef.checkIn), RoomOrderVO.class);
    }
}
