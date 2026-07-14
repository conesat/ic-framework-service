package cn.icframework.project.module.energy.swapbattery.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.Response;
import cn.icframework.project.module.energy.swapbattery.pojo.vo.SwapBatteryVO;
import cn.icframework.project.module.energy.swapbattery.service.SwapBatteryService;
import cn.icframework.project.module.energy.swapbattery.wrapperbuilder.SwapBatteryWrapperBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 小程序可用电池查询接口。 */
@RequireAuth
@RestController
@RequestMapping(value = Api.API_APP + "/swap-battery", name = "换电电池")
@RequiredArgsConstructor
public class ApiAppSwapBattery extends BasicApi {
    private final SwapBatteryService swapBatteryService;
    private final SwapBatteryWrapperBuilder wrapperBuilder;

    @GetMapping("/available")
    public Response<List<SwapBatteryVO>> available(@RequestParam Long stationId) {
        QueryParams params = new QueryParams();
        params.put("stationId", stationId);
        params.put("status", 1);
        return Response.success(swapBatteryService.select(wrapperBuilder.build(params), SwapBatteryVO.class));
    }
}
