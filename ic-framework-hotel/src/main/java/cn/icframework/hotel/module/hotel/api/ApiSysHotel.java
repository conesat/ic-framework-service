package cn.icframework.hotel.module.hotel.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.hotel.pojo.dto.HotelDTO;
import cn.icframework.hotel.module.hotel.pojo.vo.HotelVO;
import cn.icframework.hotel.module.hotel.pojo.vo.HotelVOConverter;
import cn.icframework.hotel.module.hotel.service.HotelService;
import cn.icframework.hotel.module.hotel.wrapperbuilder.HotelWrapperBuilder;
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
 * @date 2024/09/24
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/hotel", name = "酒店")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysHotel extends BasicApi {
    private final HotelService hotelService;
    private final HotelVOConverter hotelVOConverter;
    private final HotelWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<HotelVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return Response.success(hotelService.selectOne(sqlWrapper, HotelVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<HotelVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return hotelService.page(sqlWrapper, page, HotelVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<HotelVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return hotelVOConverter.convert(hotelService.select(sqlWrapper));
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        hotelService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated HotelDTO dto) {
        hotelService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated HotelDTO dto) {
        hotelService.edit(dto);
        return Response.success();
    }
}
