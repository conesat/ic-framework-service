package cn.icframework.hotel.module.serverorder.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.serverorder.pojo.dto.ServerOrderDTO;
import cn.icframework.hotel.module.serverorder.pojo.vo.ServerOrderVO;
import cn.icframework.hotel.module.serverorder.pojo.vo.ServerOrderVOConverter;
import cn.icframework.hotel.module.serverorder.service.ServerOrderService;
import cn.icframework.hotel.module.serverorder.wrapperbuilder.ServerOrderWrapperBuilder;
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
 * @date 2024/11/29
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/server-order", name = "服务订单")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysServerOrder extends BasicApi {
    private final ServerOrderService serverOrderService;
    private final ServerOrderVOConverter serverOrderVOConverter;
    private final ServerOrderWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<ServerOrderVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        return Response.success(serverOrderVOConverter.convert(serverOrderService.selectOne(wrapperBuilder.build(queryParams))));
    }

    /**
     * 根据消费订单获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item-by-consume-order-id/{id}", name = "获取详情")
    public Response<ServerOrderVO> itemByConsumeOrderId(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        return Response.success(serverOrderVOConverter.convert(serverOrderService.selectOne(wrapperBuilder.build(queryParams))));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ServerOrderVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return serverOrderVOConverter.convert(serverOrderService.page(sqlWrapper, page));
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<ServerOrderVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return serverOrderVOConverter.convert(serverOrderService.select(sqlWrapper));
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        serverOrderService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated ServerOrderDTO dto) {
        serverOrderService.edit(dto);
        return Response.success();
    }

    /**
     * 指派
     */
    @PutMapping(value = "/assign", name = "指派")
    public Response<Void> assign(@RequestParam("userId") Long userId, @RequestParam("ids") Long[] ids) {
        serverOrderService.assign(userId, ids);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated ServerOrderDTO dto) {
        serverOrderService.edit(dto);
        return Response.success();
    }
}
