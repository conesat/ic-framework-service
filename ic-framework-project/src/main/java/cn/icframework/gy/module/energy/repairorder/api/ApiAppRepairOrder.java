package cn.icframework.project.module.energy.repairorder.api;

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
import cn.icframework.project.module.energy.repairorder.RepairOrder;
import cn.icframework.project.module.energy.repairorder.pojo.vo.RepairOrderVO;
import cn.icframework.project.module.energy.repairorder.service.RepairOrderService;
import cn.icframework.project.module.energy.repairorder.wrapperbuilder.RepairOrderWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 小程序的维修上报和工单查询接口。 */
@RequireAuth
@RestController
@RequestMapping(value = Api.API_APP + "/repair-order", name = "维修工单")
@RequiredArgsConstructor
public class ApiAppRepairOrder extends BasicApi {
    private final RepairOrderService repairOrderService;
    private final RepairOrderWrapperBuilder wrapperBuilder;

    @PostMapping("/page")
    public PageResponse<RepairOrderVO> page(HttpServletRequest request, PageRequest page) {
        QueryParams params = getQueryParams(request);
        params.put("userId", JWTUtils.getUserId());
        SqlWrapper sqlWrapper = wrapperBuilder.build(params);
        return repairOrderService.page(sqlWrapper, page, RepairOrderVO.class);
    }

    @GetMapping("/item/{id}")
    public Response<RepairOrderVO> detail(@PathVariable Long id) {
        RepairOrder order = repairOrderService.selectById(id);
        Assert.isNotNull(order, "工单不存在");
        Assert.isTrue(JWTUtils.getUserId().equals(order.getUserId()), "无权查看该工单");
        return Response.success(repairOrderService.selectById(id, RepairOrderVO.class));
    }

    @PostMapping("/create")
    public Response<RepairOrderVO> create(@RequestParam(required = false) Long stationId,
                                          @RequestParam String repairType,
                                          @RequestParam String description,
                                          @RequestParam(value = "images", required = false) String images,
                                          @RequestParam(value = "contactPhone", required = false) String contactPhone) {
        RepairOrder order = repairOrderService.createForUser(JWTUtils.getUserId(), stationId, repairType, description, images, contactPhone);
        return Response.success(repairOrderService.selectById(order.getId(), RepairOrderVO.class));
    }

    @PostMapping("/cancel/{id}")
    public Response<Void> cancel(@PathVariable Long id) {
        repairOrderService.cancelForUser(JWTUtils.getUserId(), id);
        return Response.success();
    }
}
