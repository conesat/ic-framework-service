package cn.icframework.hotel.module.hoteluser.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.hotel.module.hoteluser.pojo.dto.HotelUserDTO;
import cn.icframework.hotel.module.hoteluser.pojo.vo.HotelUserVO;
import cn.icframework.hotel.module.hoteluser.pojo.vo.HotelUserVOConverter;
import cn.icframework.hotel.module.hoteluser.service.HotelUserService;
import cn.icframework.hotel.module.hoteluser.wrapperbuilder.HotelUserWrapperBuilder;
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
 * @date 2024/10/25
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/hotel-user", name ="酒店用户")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysHotelUser extends BasicApi {
    private final HotelUserService hotelUserService;
    private final HotelUserVOConverter hotelUserVOConverter;
    private final HotelUserWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<HotelUserVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(hotelUserService.selectById(id, HotelUserVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<HotelUserVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return hotelUserService.page(sqlWrapper, page, HotelUserVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    //@PostMapping(value = "/all", name = "查询全部")
    //public List<HotelUserVO> all(HttpServletRequest request) {
    //    SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
    //    return hotelUserVOConverter.convert(hotelUserService.select(sqlWrapper));
    //}

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        hotelUserService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated HotelUserDTO dto) {
        hotelUserService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated HotelUserDTO dto) {
        hotelUserService.edit(dto);
        return Response.success();
    }
}
