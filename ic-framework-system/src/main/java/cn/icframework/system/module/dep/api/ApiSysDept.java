package cn.icframework.system.module.dep.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.dep.pojo.dto.DeptDTO;
import cn.icframework.system.module.dep.pojo.vo.DeptVO;
import cn.icframework.system.module.dep.service.DeptService;
import cn.icframework.system.module.dep.wrapperbuilder.DeptWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author create by ic gen
 * @date 2023/06/21
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/dept", name = "部门")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysDept extends BasicApi {
    private final DeptService deptService;
    private final DeptWrapperBuilder deptWrapperBuilder;

    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return Response<DeptVO>
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<DeptVO> detail(@PathVariable("id") Serializable id) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", id);
        SqlWrapper sqlWrapper = deptWrapperBuilder.build(queryParams);
        return Response.success(deptService.selectOne(sqlWrapper, DeptVO.class));
    }

    /**
     * 删除
     * @param ids [Serializable[]] id列表
     * @return Response
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        deptService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑或者保存
     * @return Response
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated DeptDTO form) {
        deptService.edit(form);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated DeptDTO form) {
        deptService.edit(form);
        return Response.success();
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return PageResponse<DeptVO>
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<DeptVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = deptWrapperBuilder.build(getQueryParams(request));
        return deptService.page(sqlWrapper, page, DeptVO.class);
    }

    /**
     * 查询全部
     * @return List<DeptVO>
     */
    @PostMapping(value = "/all", name = "查询全部")
    public Response<List<DeptVO>> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = deptWrapperBuilder.build(getQueryParams(request));
        return Response.success(deptService.select(sqlWrapper, DeptVO.class));
    }

}
