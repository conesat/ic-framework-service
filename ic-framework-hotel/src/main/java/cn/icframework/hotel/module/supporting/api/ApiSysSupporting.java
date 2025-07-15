package cn.icframework.hotel.module.supporting.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.common.enums.SupportingTypeEnum;
import cn.icframework.hotel.module.supporting.pojo.dto.SupportingDTO;
import cn.icframework.hotel.module.supporting.pojo.vo.SupportingVO;
import cn.icframework.hotel.module.supporting.pojo.vo.SupportingVOConverter;
import cn.icframework.hotel.module.supporting.service.SupportingService;
import cn.icframework.hotel.module.supporting.wrapperbuilder.SupportingWrapperBuilder;
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
@RequestMapping(value = Api.API_SYS + "/supporting", name ="配套设施")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysSupporting extends BasicApi {
    private final SupportingService supportingService;
    private final SupportingVOConverter supportingVOConverter;
    private final SupportingWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<SupportingVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(supportingService.selectById(id, SupportingVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<SupportingVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return supportingService.page(sqlWrapper, page, SupportingVO.class);
    }

    /**
     * 获取配套设施类型
     *
     * @return
     */
    @GetMapping(value = "/types", name = "获取配套设施类型")
    public Response<SupportingTypeEnum[]> types() {
        return Response.success(SupportingTypeEnum.values());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<SupportingVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return supportingService.select(sqlWrapper, SupportingVO.class);
    }


    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        supportingService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated SupportingDTO dto) {
        supportingService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated SupportingDTO dto) {
        supportingService.edit(dto);
        return Response.success();
    }
}
