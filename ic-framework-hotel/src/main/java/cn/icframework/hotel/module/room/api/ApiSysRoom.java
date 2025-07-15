package cn.icframework.hotel.module.room.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.common.enums.RoomStatusEnum;
import cn.icframework.hotel.module.room.pojo.dto.RoomDTO;
import cn.icframework.hotel.module.room.pojo.dto.RoomStatusDTO;
import cn.icframework.hotel.module.room.pojo.dto.RoomUpdatePriceDTO;
import cn.icframework.hotel.module.room.pojo.dto.RoomUpdateStatusDTO;
import cn.icframework.hotel.module.room.pojo.vo.*;
import cn.icframework.hotel.module.room.service.RoomService;
import cn.icframework.hotel.module.room.wrapperbuilder.RoomWrapperBuilder;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/09/25
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/room", name = "房间 ")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysRoom extends BasicApi {
    private final RoomService roomService;
    private final RoomVOConverter roomVOConverter;
    private final RoomWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<RoomDetailVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put(RoomWrapperBuilder.BuildColumn.id, id);
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return Response.success(roomService.selectOne(sqlWrapper, RoomDetailVO.class));
    }

    /**
     * 根据外部key获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/detail-by-ext-key/{extKey}", name = "根据外部key获取单个详情")
    public Response<RoomDetailVO> detailByExtKey(@PathVariable("extKey") Serializable extKey) {
        QueryParams queryParams = new QueryParams();
        queryParams.put(RoomWrapperBuilder.BuildColumn.extKey, extKey);
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return Response.success(roomService.selectOne(sqlWrapper, RoomDetailVO.class));
    }

    /**
     * 楼栋统计
     *
     * @param buildingId [Serializable] *buildingId
     * @param floorId    [Serializable] *floorId
     * @return
     */
    @GetMapping(value = "/statics", name = "楼栋统计")
    public Response<List<RoomStaticsVO>> statics(Long buildingId, Long floorId) {
        return Response.success(roomService.statics(buildingId, floorId));
    }

    /**
     * 获取列表
     *
     * @param pageIndex [int] 当前页码
     * @param pageSize  [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<RoomForIndexVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return roomService.page(sqlWrapper, page, RoomForIndexVO.class);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询所有")
    public List<RoomForIndexVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return roomService.select(sqlWrapper, RoomForIndexVO.class);
    }

    /**
     * 查询全部状态枚举
     *
     * @return
     */
    @GetMapping(value = "/room-status-enums", name = "查询全部")
    public Response<RoomStatusEnum[]> roomStatusEnums() {
        return Response.success(RoomStatusEnum.values());
    }

    /**
     * 查询全部房态
     *
     * @return
     */
    @GetMapping(value = "/room-status", name = "查询全部房态")
    public PageResponse<RoomStatusVO> roomStatus(HttpServletRequest request, RoomStatusDTO dto, PageRequest page) {
        if (dto.getStartDate() == null) {
            dto.setStartDate(LocalDate.now());
        }
        if (dto.getEndDate() == null) {
            dto.setEndDate(LocalDate.now().plusDays(30));
        }
        QueryParams queryParams = getQueryParams(request);
        return roomService.roomStatus(queryParams, dto, page);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        roomService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated RoomDTO dto) {
        roomService.edit(dto);
        return Response.success();
    }

    /**
     * 批量更新价格
     */
    @PutMapping(value = "/update-price", name = "批量更新价格")
    public Response<Void> updatePrice(@Validated RoomUpdatePriceDTO dto) {
        roomService.updatePrice(dto);
        return Response.success();
    }

    /**
     * 批量更新状态
     */
    @PutMapping(value = "/update-status", name = "批量更新状态")
    public Response<Void> updateStatus(@Validated RoomUpdateStatusDTO dto) {
        roomService.updateStatus(dto);
        return Response.success();
    }

    /**
     * 批量外部关联
     */
    @PutMapping(value = "/update-ext-key", name = "批量外部关联")
    public Response<Void> updateExtKey(@RequestParam Long roomId, @RequestParam String extKey) {
        roomService.updateExtKey(roomId, extKey);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated RoomDTO dto, @RequestParam List<String> nos) {
        roomService.addBatch(dto, nos);
        return Response.success();
    }
}
