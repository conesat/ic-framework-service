package cn.icframework.hotel.module.building.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.building.pojo.dto.BuildingDTO;
import cn.icframework.hotel.module.building.pojo.vo.BuildingVO;
import cn.icframework.hotel.module.building.pojo.vo.BuildingVOConverter;
import cn.icframework.hotel.module.building.service.BuildingService;
import cn.icframework.hotel.module.building.wrapperbuilder.BuildingWrapperBuilder;
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
 * @date 2024/09/25
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/building", name ="楼栋")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysBuilding extends BasicApi {
    private final BuildingService buildingService;
    private final BuildingVOConverter buildingVOConverter;
    private final BuildingWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<BuildingVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return Response.success(buildingService.selectOne(sqlWrapper, BuildingVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<BuildingVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return buildingService.page(sqlWrapper, page, BuildingVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<BuildingVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return buildingVOConverter.convert(buildingService.select(sqlWrapper));
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        buildingService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated BuildingDTO dto) {
        buildingService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated BuildingDTO dto) {
        buildingService.edit(dto);
        return Response.success();
    }
}
