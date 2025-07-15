package cn.icframework.system.module.depuser.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.depuser.pojo.dto.DepUserDTO;
import cn.icframework.system.module.depuser.pojo.vo.DepUserDetailVO;
import cn.icframework.system.module.depuser.pojo.vo.DepUserVO;
import cn.icframework.system.module.depuser.service.DepUserService;
import cn.icframework.system.module.depuser.wrapperbuilder.DepUserDetailWrapperBuilder;
import cn.icframework.system.module.depuser.wrapperbuilder.DepUserWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/08/11
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/dep-user", name = "部门用户")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysDepUser extends BasicApi {
    private final DepUserService depUserService;
    private final DepUserWrapperBuilder wrapperBuilder;
    private final DepUserDetailWrapperBuilder depUserDetailWrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<DepUserVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(depUserService.selectById(id, DepUserVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<DepUserVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return depUserService.page(sqlWrapper, page, DepUserVO.class);
    }

    /**
     * 获取详情列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page-detail", name = "获取详情列表")
    public PageResponse<DepUserDetailVO> pageDetail(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = depUserDetailWrapperBuilder.build(getQueryParams(request));
        return depUserService.page(sqlWrapper, page, DepUserDetailVO.class);
    }


    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        depUserService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated DepUserDTO dto) {
        depUserService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@RequestParam Long depId, @RequestParam Long[] userIds) {
        depUserService.createBatch(depId, userIds);
        return Response.success();
    }

    /**
     * 设置为负责人
     *
     * @param id [long] 部门用户id
     * @return
     */
    @PostMapping(value = "/manager/{id}", name = "设置部门负责人")
    public Response<Void> setManager(@PathVariable("id") Long id, @RequestParam Boolean manager) {
        depUserService.setManager(depUserService.selectById(id), manager);
        return Response.success();
    }

}
