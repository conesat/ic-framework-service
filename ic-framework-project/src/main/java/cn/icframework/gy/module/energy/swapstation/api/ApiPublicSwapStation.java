package cn.icframework.project.module.energy.swapstation.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.Response;
import cn.icframework.project.module.energy.swapstation.pojo.vo.SwapStationVO;
import cn.icframework.project.module.energy.swapstation.service.SwapStationService;
import cn.icframework.project.module.energy.swapstation.wrapperbuilder.SwapStationWrapperBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 无需登录即可浏览的换电站接口。
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/swap-station", name = "换电站")
@RequiredArgsConstructor
public class ApiPublicSwapStation extends BasicApi {
    private final SwapStationService swapStationService;
    private final SwapStationWrapperBuilder wrapperBuilder;

    /** 获取营业中的附近站点（当前按库存和评分推荐）。 */
    @GetMapping("/nearby")
    public Response<List<SwapStationVO>> nearby(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        QueryParams params = new QueryParams();
        params.put("status", 1);
        int safeLimit = Math.max(1, Math.min(limit == null ? 10 : limit, 30));
        List<SwapStationVO> stations = swapStationService.select(wrapperBuilder.build(params), SwapStationVO.class)
            .stream()
            .limit(safeLimit)
            .toList();
        return Response.success(stations);
    }

    /** 获取站点详情。 */
    @GetMapping("/item/{id}")
    public Response<SwapStationVO> detail(@PathVariable Long id) {
        return Response.success(swapStationService.selectById(id, SwapStationVO.class));
    }
}
