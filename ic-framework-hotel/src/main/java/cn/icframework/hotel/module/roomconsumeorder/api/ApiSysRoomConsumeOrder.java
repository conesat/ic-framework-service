package cn.icframework.hotel.module.roomconsumeorder.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.roomconsumeorder.pojo.dto.RoomConsumeOrderBatchDTO;
import cn.icframework.hotel.module.roomconsumeorder.pojo.dto.RoomConsumeOrderDTO;
import cn.icframework.hotel.module.roomconsumeorder.pojo.vo.RoomConsumeOrderVO;
import cn.icframework.hotel.module.roomconsumeorder.pojo.vo.RoomConsumeOrderVOConverter;
import cn.icframework.hotel.module.roomconsumeorder.service.RoomConsumeOrderService;
import cn.icframework.hotel.module.roomconsumeorder.wrapperbuilder.RoomConsumeOrderWrapperBuilder;
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
@RequestMapping(value = Api.API_SYS + "/room-consume-order", name = "额外消费")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysRoomConsumeOrder extends BasicApi {
    private final RoomConsumeOrderService roomConsumeOrderService;
    private final RoomConsumeOrderVOConverter roomConsumeOrderVOConverter;
    private final RoomConsumeOrderWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<RoomConsumeOrderVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        return Response.success(roomConsumeOrderService.selectOne(wrapperBuilder.build(queryParams), RoomConsumeOrderVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<RoomConsumeOrderVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return roomConsumeOrderService.page(sqlWrapper, page, RoomConsumeOrderVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<RoomConsumeOrderVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return roomConsumeOrderService.select(sqlWrapper, RoomConsumeOrderVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        roomConsumeOrderService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated RoomConsumeOrderDTO dto) {
        roomConsumeOrderService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated RoomConsumeOrderDTO dto) {
        roomConsumeOrderService.edit(dto);
        return Response.success();
    }

    /**
     * 批量新增
     */
    @PostMapping(value = "/create-batch", name = "批量新增")
    public Response<Void> createBatch(@RequestBody RoomConsumeOrderBatchDTO form) {
        roomConsumeOrderService.createBatch(form);
        return Response.success();
    }
}
