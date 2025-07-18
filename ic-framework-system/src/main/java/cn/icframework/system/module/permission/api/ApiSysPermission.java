package cn.icframework.system.module.permission.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.permission.pojo.vo.PermissionVO;
import cn.icframework.system.module.permission.service.PermissionService;
import cn.icframework.system.module.permission.wrapperbuilder.PermissionWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * @author ic generator
 * @since 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/permission", name = "权限")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysPermission extends BasicApi {
    private final PermissionService permissionService;
    private final PermissionWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<PermissionVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(permissionService.selectById(id, PermissionVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<PermissionVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return permissionService.page(sqlWrapper, page, PermissionVO.class);
    }

    /**
     * 查询全部权限
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<PermissionVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return permissionService.select(sqlWrapper, PermissionVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(value = "/delete", name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        permissionService.deleteByIds(ids);
        return Response.success();
    }
}
