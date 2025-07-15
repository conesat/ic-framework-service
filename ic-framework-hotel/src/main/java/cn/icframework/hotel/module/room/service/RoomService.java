package cn.icframework.hotel.module.room.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.core.utils.LocalDateTimeUtils;
import cn.icframework.hotel.common.enums.RoomStatusEnum;
import cn.icframework.hotel.common.enums.ServerStateEnum;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import cn.icframework.hotel.module.floor.def.FloorDef;
import cn.icframework.hotel.module.room.Room;
import cn.icframework.hotel.module.room.dao.RoomMapper;
import cn.icframework.hotel.module.room.def.RoomDef;
import cn.icframework.hotel.module.room.pojo.dto.RoomDTO;
import cn.icframework.hotel.module.room.pojo.dto.RoomStatusDTO;
import cn.icframework.hotel.module.room.pojo.dto.RoomUpdatePriceDTO;
import cn.icframework.hotel.module.room.pojo.dto.RoomUpdateStatusDTO;
import cn.icframework.hotel.module.room.pojo.vo.RoomStaticsVO;
import cn.icframework.hotel.module.room.pojo.vo.RoomStatusItemVO;
import cn.icframework.hotel.module.room.pojo.vo.RoomStatusVO;
import cn.icframework.hotel.module.room.pojo.vo.RoomVO;
import cn.icframework.hotel.module.room.wrapperbuilder.RoomWrapperBuilder;
import cn.icframework.hotel.module.roomorder.def.RoomOrderDef;
import cn.icframework.hotel.module.roomorder.pojo.vo.RoomOrderSimpleVO;
import cn.icframework.hotel.module.roomorder.service.RoomOrderService;
import cn.icframework.hotel.module.roompic.RoomPic;
import cn.icframework.hotel.module.roompic.def.RoomPicDef;
import cn.icframework.hotel.module.roompic.pojo.dto.RoomPicSimpleDTO;
import cn.icframework.hotel.module.roompic.service.RoomPicService;
import cn.icframework.hotel.module.roomprice.RoomPrice;
import cn.icframework.hotel.module.roomprice.def.RoomPriceDef;
import cn.icframework.hotel.module.roomprice.service.RoomPriceService;
import cn.icframework.hotel.module.roomsupporting.RoomSupporting;
import cn.icframework.hotel.module.roomsupporting.def.RoomSupportingDef;
import cn.icframework.hotel.module.roomsupporting.service.RoomSupportingService;
import cn.icframework.hotel.module.serverorder.ServerOrder;
import cn.icframework.hotel.module.serverorder.def.ServerOrderDef;
import cn.icframework.hotel.module.serverorder.service.ServerOrderService;
import cn.icframework.mybatis.query.Checks;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import com.alibaba.fastjson.JSONArray;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static cn.icframework.mybatis.query.Checks.CHECK;
import static cn.icframework.mybatis.wrapper.FunctionWrapper.COUNT;

/**
 * @author ic
 * @date 2024/09/25
 */
@Service
@RequiredArgsConstructor
public class RoomService extends BasicService<RoomMapper, Room> {

    private final RoomSupportingService roomSupportingService;
    private final RoomPicService roomPicService;
    private final RoomPriceService roomPriceService;
    private final RoomOrderService roomOrderService;
    private final RoomWrapperBuilder wrapperBuilder;
    private final ServerOrderService serverOrderService;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(RoomDTO dto) {
        edit(dto, true);
    }

    private void edit(RoomDTO dto, boolean check) {
        if (StringUtils.isNotEmpty(dto.getExtKey())) {
            Room room = selectOne(RoomDef.table().extKey.eq(dto.getExtKey()));
            Assert.isNull(room, "外部key：" + dto.getExtKey() + "已被" + room.getNo() + "使用");
        }

        Room entity = dto.getId() != null ? selectById(dto.getId()) : Room.def();
        BeanUtils.copyExcludeProps(dto, entity);
        RoomDef roomDef = RoomDef.table();
        if (check) {
            Long dbId = selectOne(SELECT(roomDef.id).FROM(roomDef).WHERE(roomDef.id.ne(dto.getId()).no.eq(dto.getNo()).floorId.eq(dto.getFloorId())), Long.class);
            Assert.isNull(dbId, "房间号" + entity.getNo() + "已存在");
        }
        if (dto.getId() != null) {
            updateById(entity);
            handlerRoomPic(dto, true, entity);
        } else {
            insert(entity);
            handlerRoomPic(dto, false, entity);
        }
        // 删除原本的配套设施
        handleSupporting(dto, entity);
    }

    /**
     * 除了房间图片关联
     *
     * @param dto
     * @param update
     * @param entity
     */
    private void handlerRoomPic(RoomDTO dto, boolean update, Room entity) {
        RoomPicDef roomPicDef = RoomPicDef.table();
        List<RoomPicSimpleDTO> picFiles = StringUtils.isEmpty(dto.getPicFiles()) ? Collections.emptyList() : JSONArray.parseArray(dto.getPicFiles(), RoomPicSimpleDTO.class);
        Set<Long> dbRoomPicFileIdSet = new HashSet<>();
        if (update) {
            List<Long> fileIds = picFiles.stream().map(RoomPicSimpleDTO::getFileId).collect(Collectors.toList());
            // 删除被移除的图片
            SqlWrapper deleteWrapper = DELETE_FROM(roomPicDef).WHERE(roomPicDef.roomId.eq(entity.getId()));
            if (!fileIds.isEmpty()) {
                deleteWrapper.WHERE(roomPicDef.fileId.notIn(fileIds));
                List<Long> dbRoomPicFileIdList = roomPicService.select(SELECT(roomPicDef.fileId).FROM(roomPicDef).WHERE(roomPicDef.roomId.eq(entity.getId()).fileId.in(fileIds)), Long.class);
                dbRoomPicFileIdSet = new HashSet<>(dbRoomPicFileIdList);
            }
            roomPicService.delete(deleteWrapper);
        }

        Long mainPicId = null;
        List<RoomPic> roomPics = new ArrayList<>();
        for (RoomPicSimpleDTO picFile : picFiles) {
            if (!dbRoomPicFileIdSet.contains(picFile.getFileId())) {
                RoomPic roomPic = RoomPic.def();
                roomPic.setFileUrl(picFile.getFileUrl());
                roomPic.setFileId(picFile.getFileId());
                roomPic.setRoomId(entity.getId());
                roomPics.add(roomPic);
            }
            if (picFile.getMainPic() != null && picFile.getMainPic()) {
                mainPicId = picFile.getFileId();
            }
        }
        if (!roomPics.isEmpty()) {
            roomPicService.insertBatch(roomPics, false, false);
        }
        if (mainPicId != null) {
            Long dbMainPicId = roomPicService.selectOne(SELECT(roomPicDef.fileId).FROM(roomPicDef).WHERE(roomPicDef.roomId.eq(entity.getId()).mainPic.eq(true)), Long.class);
            if (!Objects.equals(dbMainPicId, mainPicId)) {
                // 移除默认封面
                roomPicService.update(UPDATE(roomPicDef).SET(roomPicDef.mainPic.set(false)).WHERE(roomPicDef.roomId.eq(entity.getId())));
                // 添加默认封面
                roomPicService.update(UPDATE(roomPicDef).SET(roomPicDef.mainPic.set(true)).WHERE(roomPicDef.fileId.eq(mainPicId).roomId.eq(entity.getId())));
            }
        }
    }

    /**
     * 处理房间配套关联
     *
     * @param dto
     * @param entity
     */
    private void handleSupporting(RoomDTO dto, Room entity) {
        RoomSupportingDef roomSupportingDef = RoomSupportingDef.table();
        roomSupportingService.delete(DELETE_FROM(roomSupportingDef).WHERE(roomSupportingDef.roomId.eq(entity.getId())));
        if (dto.getSupportingIds() != null && dto.getSupportingIds().length > 0) {
            List<RoomSupporting> roomSupportingList = new ArrayList<>(dto.getSupportingIds().length);
            for (Long supportingId : dto.getSupportingIds()) {
                RoomSupporting roomSupporting = RoomSupporting.def();
                roomSupporting.setRoomId(entity.getId());
                roomSupporting.setSupportingId(supportingId);
                roomSupportingList.add(roomSupporting);
            }
            roomSupportingService.insertBatch(roomSupportingList);
        }
    }

    @Transactional
    public void addBatch(RoomDTO dto, List<String> nos) {
        RoomDef roomDef = RoomDef.table();
        List<String> dbNos = select(SELECT(roomDef.no).FROM(roomDef).WHERE(roomDef.no.in(nos).floorId.eq(dto.getFloorId())), String.class);
        Assert.isTrue(dbNos == null || dbNos.isEmpty(), "房间号[" + String.join(",", dbNos) + "]已存在");
        for (String no : new HashSet<>(nos)) {
            dto.setNo(no);
            dto.setId(null);
            edit(dto, false);
        }
    }

    public PageResponse<RoomStatusVO> roomStatus(QueryParams queryParams, RoomStatusDTO dto, PageRequest page) {
        PageResponse<RoomStatusVO> pageRes = new PageResponse<>();
        // 获取分页数据
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        PageResponse<RoomVO> pageResponse = page(sqlWrapper, page, RoomVO.class);
        pageRes.setIndex(pageResponse.getIndex());
        pageRes.setSize(pageResponse.getSize());
        pageRes.setTotal(pageResponse.getTotal());
        pageRes.setPages(pageResponse.getPages());
        List<RoomVO> data = pageResponse.getRecords();

        List<Long> roomIds = data.stream().map(RoomVO::getId).collect(Collectors.toList());
        if (roomIds.isEmpty()) {
            return pageRes;
        }
        // 查找价格列表
        RoomPriceDef roomPriceDef = RoomPriceDef.table();
        List<RoomPrice> roomPriceList = roomPriceService.select(SELECT().FROM(roomPriceDef).WHERE(roomPriceDef.roomId.in(roomIds).date.ge(dto.getStartDate()).date.le(dto.getEndDate())));
        Map<String, RoomPrice> roomPriceMap = roomPriceList.stream().collect(Collectors.toMap(f -> f.getRoomId().toString() + f.getDate(), f -> f));
        // 查找订单
        RoomOrderDef roomOrderDef = RoomOrderDef.table();
        List<RoomOrderSimpleVO> roomOrderList = roomOrderService.select(SELECT()
                        .FROM(roomOrderDef)
                        .WHERE(roomOrderDef.roomId.in(roomIds).outDate.ge(dto.getStartDate()).inDate.le(dto.getEndDate()))
                        .ORDER_BY_ASC(roomOrderDef.inDate)
                , RoomOrderSimpleVO.class);

        // 补全入住多日的订单中间状态
        Map<String, List<RoomOrderSimpleVO>> roomOrderMap = roomOrderList.stream().collect(
                Collectors.groupingBy(f -> f.getRoomId() + LocalDateTimeUtils.getFormatDate(LocalDateTimeUtils.TIME_FORMAT_3STR, f.getInDate().isBefore(dto.getStartDate()) ? dto.getStartDate() : f.getInDate()))
        );

        long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate());
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate localDate = dto.getStartDate().plusDays(i);
            dates.add(localDate);
        }
        List<RoomStatusVO> res = new ArrayList<>();
        for (RoomVO roomVO : data) {
            RoomStatusVO roomStatusVO = new RoomStatusVO();
            roomStatusVO.setRoom(roomVO);

            long jup = 0;
            RoomStatusItemVO beforeVO = null;
            for (LocalDate date : dates) {
                RoomStatusItemVO roomStatusItemVO = new RoomStatusItemVO();
                if (jup > 0) {
                    jup--;
                    beforeVO.setDayOfWeek(date.getDayOfWeek().getValue());
                    roomStatusItemVO.setShow(false);
                } else {
                    roomStatusItemVO.setShow(true);
                }
                String formatDate = LocalDateTimeUtils.getFormatDate("yyyy-MM-dd", date);
                roomStatusItemVO.setDate(formatDate);
                RoomPrice roomPrice = roomPriceMap.get(roomVO.getId() + formatDate);
                List<RoomOrderSimpleVO> roomOrders = roomOrderMap.get(roomVO.getId() + formatDate);
                RoomOrderSimpleVO roomOrder = null;
                if (roomOrders != null && !roomOrders.isEmpty()) {
                    roomOrder = roomOrders.get(0);
                }
                if (roomPrice != null) {
                    roomStatusItemVO.setPrice(roomPrice.getPrice());
                } else if ((date.getDayOfWeek().getValue() == 7 || date.getDayOfWeek().getValue() == 6) && roomVO.getWeekPrice() != null) {
                    roomStatusItemVO.setPrice(roomVO.getWeekPrice());
                } else {
                    roomStatusItemVO.setPrice(roomVO.getPrice());
                }
                if (roomOrder != null) {
                    jup = ChronoUnit.DAYS.between(roomOrder.getInDate().isBefore(dto.getStartDate()) ? dto.getStartDate() : roomOrder.getInDate(), roomOrder.getOutDate()) - 1;
                    roomOrder.setRemainingDay((int) jup);
                    beforeVO = roomStatusItemVO;
                    roomStatusItemVO.setRoomStatus(roomOrder.getCheckIn() != null && roomOrder.getCheckIn() ? RoomStatusEnum.CHECKED_IN : RoomStatusEnum.RESERVE);
                } else {
                    roomStatusItemVO.setRoomStatus(jup == 0 ? RoomStatusEnum.AWAIT_CHECK_IN : beforeVO.getRoomStatus());
                }
                roomStatusItemVO.setDayOfWeek(date.getDayOfWeek().getValue());
                roomStatusItemVO.setSaleStatus(roomVO.getSaleStatus());
                roomStatusItemVO.setOrder(roomOrder);
                roomStatusVO.getItems().add(roomStatusItemVO);
            }
            res.add(roomStatusVO);
        }
        pageRes.setRecords(res);
        return pageRes;
    }

    /**
     * 批量更新价格
     *
     * @param dto
     */
    public void updatePrice(RoomUpdatePriceDTO dto) {
        Assert.isFalse(dto.getDateStart().isAfter(dto.getDateEnd()), "开始时间不能大于结束时间");
        RoomPriceDef roomPriceDef = RoomPriceDef.table();
        while (dto.getDateStart().isBefore(dto.getDateEnd()) || dto.getDateStart().isEqual(dto.getDateEnd())) {
            RoomPrice roomPrice = roomPriceService.selectOne(SELECT().FROM(roomPriceDef).WHERE(roomPriceDef.roomId.eq(dto.getRoomId()).date.eq(dto.getDateStart())));
            if (roomPrice == null) {
                roomPrice = RoomPrice.def();
                roomPrice.setRoomId(dto.getRoomId());
                roomPrice.setDate(dto.getDateStart());
                roomPrice.setPrice(dto.getPrice());
                roomPriceService.insert(roomPrice);
            } else {
                roomPrice.setPrice(dto.getPrice());
                roomPriceService.updateById(roomPrice);
            }
            dto.setDateStart(dto.getDateStart().plusDays(1));
        }
    }

    /**
     * 批量更新状态
     *
     * @param dto
     */
    @Transactional
    public void updateStatus(RoomUpdateStatusDTO dto) {
        Assert.isFalse(dto.getDateStart().isBefore(LocalDate.now()), "仅能修改今日及往后日期");
        Assert.isFalse(dto.getDateStart().isAfter(dto.getDateEnd()), "开始时间不能大于结束时间");
        ServerOrderDef serverOrderDef = ServerOrderDef.table();

        serverOrderService.delete(DELETE_FROM(serverOrderDef)
                .WHERE(serverOrderDef
                        .target.eq(ServerTargetEnum.CLEAR.code())
                        .targetId.eq(dto.getRoomId())
                        .doTime.ge(dto.getDateStart().atTime(0, 0, 0))
                        .doTime.le(dto.getDateEnd().atTime(23, 59, 59))
                ));
        LocalDate localDate = LocalDate.now();
        Room room = selectById(dto.getRoomId());

        if (!dto.getNeedClear()) {
            return;
        }

        if (localDate.equals(dto.getDateStart())) {
            // 当日需要修改房间状态
            RoomStatusEnum roomStatus = room.getRoomStatus();
            Assert.isFalse(roomStatus == RoomStatusEnum.UNDER_MAINTENANCE
                            || roomStatus == RoomStatusEnum.CHECKED_IN_CLEANING
                            || roomStatus == RoomStatusEnum.CLEANING,
                    "房间目前处于" + roomStatus.text() + "状态不可打扫");
            if (roomStatus == RoomStatusEnum.CHECKED_IN) {
                room.setRoomStatus(RoomStatusEnum.CHECKED_IN_NEED_CLEANING);
            } else if (roomStatus == RoomStatusEnum.AWAIT_CHECK_IN || roomStatus == RoomStatusEnum.RESERVE) {
                room.setRoomStatus(RoomStatusEnum.CHECKED_OUT_PENDING_CLEANING);
            }
            RoomDef roomDef = RoomDef.table();
            update(UPDATE(roomDef)
                    .SET(roomDef.roomStatus.set(room.getRoomStatus()))
                    .WHERE(roomDef.id.eq(dto.getRoomId())));
        }
        while (dto.getDateStart().isBefore(dto.getDateEnd()) || dto.getDateStart().isEqual(dto.getDateEnd())) {
            if (dto.getClearUserId() != null) {
                ServerOrder serverOrder = ServerOrder.def();
                serverOrder.setTargetId(dto.getRoomId());
                serverOrder.setTarget(ServerTargetEnum.CLEAR);
                serverOrder.setState(ServerStateEnum.WAIT_START);
                serverOrder.setDoTime(dto.getDateStart().atTime(LocalTime.now()));
                serverOrder.setServerUserId(dto.getClearUserId());
                serverOrder.setTargetContent(room.getNo());
                serverOrderService.insert(serverOrder);
            } else {
                serverOrderService.autoCreate(ServerTargetEnum.CLEAR, dto.getRoomId(), room.getNo(), dto.getDateStart().atTime(LocalTime.now()));
            }
            dto.setDateStart(dto.getDateStart().plusDays(1));
        }

    }

    /**
     * 统计房间状态
     *
     * @param buildId
     * @param floorId
     * @return
     */
    public List<RoomStaticsVO> statics(Serializable buildId, Serializable floorId) {
        RoomDef roomDef = RoomDef.table();
        SqlWrapper sqlWrapper = SELECT(
                roomDef.roomStatus.as(RoomStaticsVO::getStatus),
                COUNT(roomDef.id).as(RoomStaticsVO::getCount))
                .FROM(roomDef)
                .WHERE(CHECK(floorId != null, roomDef.floorId::eq, floorId))
                .GROUP_BY(roomDef.roomStatus);
        if (buildId != null) {
            FloorDef floorDef = FloorDef.table();
            sqlWrapper = sqlWrapper.INNER_JOIN(floorDef).ON(floorDef.id.eq(roomDef.floorId))
                    .WHERE(floorDef.buildingId.eq(buildId));
        }
        return select(sqlWrapper, RoomStaticsVO.class);
    }

    public void updateExtKey(Long roomId, String extKey) {
        // 先移除原来的关联
        RoomDef roomDef = RoomDef.table();
        update(UPDATE(roomDef)
                .SET(roomDef.extKey, null)
                .WHERE(roomDef.extKey.eq(extKey)));

        // 绑定
        update(UPDATE(roomDef)
                .SET(roomDef.extKey, extKey)
                .WHERE(roomDef.id.eq(roomId)));
    }
}
