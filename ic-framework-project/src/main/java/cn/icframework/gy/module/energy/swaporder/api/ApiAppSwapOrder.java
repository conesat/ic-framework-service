package cn.icframework.project.module.energy.swaporder.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.core.utils.Assert;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.project.module.energy.swaporder.SwapOrder;
import cn.icframework.project.module.energy.swaporder.pojo.vo.SwapOrderVO;
import cn.icframework.project.module.energy.swaporder.service.SwapOrderService;
import cn.icframework.project.module.energy.swaporder.wrapperbuilder.SwapOrderWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 小程序的订单、预约、暂存接口。 */
@RequireAuth
@RestController
@RequestMapping(value = Api.API_APP + "/swap-order", name = "换电订单")
@RequiredArgsConstructor
public class ApiAppSwapOrder extends BasicApi {
    private final SwapOrderService swapOrderService;
    private final SwapOrderWrapperBuilder wrapperBuilder;

    @PostMapping("/page")
    public PageResponse<SwapOrderVO> page(HttpServletRequest request, PageRequest page) {
        QueryParams params = getQueryParams(request);
        params.put("userId", JWTUtils.getUserId());
        SqlWrapper sqlWrapper = wrapperBuilder.build(params);
        return swapOrderService.page(sqlWrapper, page, SwapOrderVO.class);
    }

    @GetMapping("/item/{id}")
    public Response<SwapOrderVO> detail(@PathVariable Long id) {
        SwapOrder order = swapOrderService.selectById(id);
        Assert.isNotNull(order, "订单不存在");
        Assert.isTrue(JWTUtils.getUserId().equals(order.getUserId()), "无权查看该订单");
        return Response.success(swapOrderService.selectById(id, SwapOrderVO.class));
    }

    /** 创建换电、暂存或租赁订单：1换电，2暂存，3租赁。 */
    @PostMapping("/create")
    public Response<SwapOrderVO> create(@RequestParam Long stationId,
                                        @RequestParam Integer orderType,
                                        @RequestParam(value = "remark", required = false) String remark) {
        SwapOrder order = swapOrderService.createForUser(JWTUtils.getUserId(), stationId, orderType, remark);
        return Response.success(swapOrderService.selectById(order.getId(), SwapOrderVO.class));
    }

    @PostMapping("/cancel/{id}")
    public Response<Void> cancel(@PathVariable Long id) {
        swapOrderService.cancelForUser(JWTUtils.getUserId(), id);
        return Response.success();
    }
}
