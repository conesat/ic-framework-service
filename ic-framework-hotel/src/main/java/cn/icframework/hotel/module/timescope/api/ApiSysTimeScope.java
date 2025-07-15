package cn.icframework.hotel.module.timescope.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.timescope.pojo.dto.TimeScopeDTO;
import cn.icframework.hotel.module.timescope.pojo.vo.TimeScopeVO;
import cn.icframework.hotel.module.timescope.pojo.vo.TimeScopeVOConverter;
import cn.icframework.hotel.module.timescope.service.TimeScopeService;
import cn.icframework.hotel.module.timescope.wrapperbuilder.TimeScopeWrapperBuilder;
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
 * @date 2024/11/26
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/time-scope", name ="时间表")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysTimeScope extends BasicApi {
    private final TimeScopeService timeScopeService;
    private final TimeScopeVOConverter timeScopeVOConverter;
    private final TimeScopeWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<TimeScopeVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        return Response.success(timeScopeService.selectOne(wrapperBuilder.build(queryParams), TimeScopeVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<TimeScopeVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return timeScopeService.page(sqlWrapper, page, TimeScopeVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    //@PostMapping(value = "/all", name = "查询全部")
    //public List<TimeScopeVO> all(HttpServletRequest request) {
    //    SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
    //    return timeScopeVOConverter.convert(timeScopeService.select(sqlWrapper));
    //}

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        timeScopeService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated TimeScopeDTO dto) {
        timeScopeService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated TimeScopeDTO dto) {
        timeScopeService.edit(dto);
        return Response.success();
    }
}
