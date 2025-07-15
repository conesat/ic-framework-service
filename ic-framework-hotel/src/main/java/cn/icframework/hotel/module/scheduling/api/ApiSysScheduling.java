package cn.icframework.hotel.module.scheduling.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.scheduling.pojo.dto.SchedulingDTO;
import cn.icframework.hotel.module.scheduling.pojo.dto.SchedulingQueryDTO;
import cn.icframework.hotel.module.scheduling.pojo.vo.SchedulingGroupVO;
import cn.icframework.hotel.module.scheduling.pojo.vo.SchedulingVO;
import cn.icframework.hotel.module.scheduling.pojo.vo.SchedulingVOConverter;
import cn.icframework.hotel.module.scheduling.service.SchedulingService;
import cn.icframework.hotel.module.scheduling.wrapperbuilder.SchedulingWrapperBuilder;
import cn.icframework.system.consts.UserType;
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
@RequestMapping(value = Api.API_SYS + "/scheduling", name = "排班")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysScheduling extends BasicApi {
    private final SchedulingService schedulingService;
    private final SchedulingVOConverter schedulingVOConverter;
    private final SchedulingWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<SchedulingVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        return Response.success(schedulingService.selectOne(wrapperBuilder.build(queryParams), SchedulingVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<SchedulingGroupVO> page(@Validated SchedulingQueryDTO queryDTO, PageRequest page) {
        return schedulingService.pageGroup(queryDTO, page);
    }

    /**
     * 查询全部
     *
     * @return
     */
    //@PostMapping(value = "/all", name = "查询全部")
    //public List<SchedulingVO> all(HttpServletRequest request) {
    //    SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
    //    return schedulingVOConverter.convert(schedulingService.select(sqlWrapper));
    //}

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam(value = "ids", required = false) List<Serializable> ids,
                                 @RequestParam(value = "userIds", required = false) List<Serializable> userIds) {
        if (userIds != null) {
            schedulingService.deleteByUserIds(userIds);
        } else {
            schedulingService.deleteByIds(ids);
        }
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated SchedulingDTO dto) {
        schedulingService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated SchedulingDTO dto) {
        schedulingService.edit(dto);
        return Response.success();
    }
}
