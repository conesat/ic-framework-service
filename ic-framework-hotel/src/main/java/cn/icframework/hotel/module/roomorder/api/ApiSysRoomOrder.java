package cn.icframework.hotel.module.roomorder.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.roomorder.pojo.dto.RoomOrderDTO;
import cn.icframework.hotel.module.roomorder.pojo.vo.RoomOrderVO;
import cn.icframework.hotel.module.roomorder.pojo.vo.RoomOrderVOConverter;
import cn.icframework.hotel.module.roomorder.service.RoomOrderService;
import cn.icframework.hotel.module.roomorder.wrapperbuilder.RoomOrderWrapperBuilder;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/10/14
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/room-order", name = "房间订单")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysRoomOrder extends BasicApi {
    private final RoomOrderService roomOrderService;
    private final RoomOrderVOConverter roomOrderVOConverter;
    private final RoomOrderWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<RoomOrderVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        return Response.success(roomOrderService.selectOne(wrapperBuilder.build(queryParams), RoomOrderVO.class));
    }

    /**
     * 获取当前房间订单信息
     *
     * @param roomId [Serializable] *roomId
     * @return
     */
    @GetMapping(value = "/room-now-order", name = "获取当前房间订单信息")
    public Response<RoomOrderVO> roomNowOrder(@RequestParam("roomId") Serializable roomId) {
        return Response.success(roomOrderService.roomNowOrder(roomId));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<RoomOrderVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return roomOrderService.page(sqlWrapper, page, RoomOrderVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        roomOrderService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated RoomOrderDTO dto) {
        roomOrderService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated RoomOrderDTO dto) {
        roomOrderService.edit(dto);
        return Response.success();
    }
}
