package cn.icframework.project.module.energy.swapstation.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.project.module.energy.swapstation.SwapStation;
import cn.icframework.project.module.energy.swapstation.pojo.dto.SwapStationDTO;
import cn.icframework.project.module.energy.swapstation.pojo.vo.SwapStationVO;
import cn.icframework.project.module.energy.swapstation.pojo.vo.SwapStationVOConverter;
import cn.icframework.project.module.energy.swapstation.service.SwapStationService;
import cn.icframework.project.module.energy.swapstation.wrapperbuilder.SwapStationWrapperBuilder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @since 2026/07/13
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/swap-station", name ="换电站")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysSwapStation extends BasicApi {
    private final SwapStationService swapStationService;
    private final SwapStationVOConverter swapStationVOConverter;
    private final SwapStationWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<SwapStationVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        return Response.success(swapStationService.selectOne(wrapperBuilder.build(queryParams), SwapStationVO.class));
    }

    /**
     * 获取列表
     *
     * @param current  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<SwapStationVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return swapStationService.page(sqlWrapper, page, SwapStationVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    //@PostMapping(value = "/all", name = "查询全部")
    //public List<SwapStationVO> all(HttpServletRequest request) {
    //    SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryMap(request));
    //    return swapStationVOConverter.convert(swapStationService.select(sqlWrapper));
    //}

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        swapStationService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated SwapStationDTO dto) {
        swapStationService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated SwapStationDTO dto) {
        swapStationService.edit(dto);
        return Response.success();
    }
}
