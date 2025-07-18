package cn.icframework.system.module.rp.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.rp.pojo.dto.RolePermissionDTO;
import cn.icframework.system.module.rp.pojo.vo.RolePermissionVO;
import cn.icframework.system.module.rp.service.RolePermissionService;
import cn.icframework.system.module.rp.wrapperbuilder.RolePermissionWrapperBuilder;
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
 * @author ic generator
 * @since 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/role-permission", name = "角色权限")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysRolePermission extends BasicApi {
    private final RolePermissionService rolePermissionService;
    private final RolePermissionWrapperBuilder rolePermissionWrapperBuilder;

    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<RolePermissionVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(rolePermissionService.selectById(id, RolePermissionVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<RolePermissionVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = rolePermissionWrapperBuilder.build(getQueryParams(request));
        return rolePermissionService.page(sqlWrapper, page, RolePermissionVO.class);
    }

    /**
     * 查询角色全部权限
     *
     * @return
     */
    @GetMapping(value = "/all-permission/{roleId}", name = "查询角色全部权限")
    public List<Long> allPermission(@PathVariable Long roleId) {
        return rolePermissionService.allPermissionIdsByRoleId(roleId);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        rolePermissionService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑或者保存
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated RolePermissionDTO dto) {
        rolePermissionService.edit(dto);
        return Response.success();
    }

    /**
     * 批量添加角色权限
     *
     * @param roleId 所属角色
     * @param permissionIds 权限列表
     * @param enable 是否启用
     */
    @PostMapping(value = "/edit-batch", name = "批量添加角色权限")
    public Response<Void> editBatch(@RequestParam Long roleId,
                                    @RequestParam Long[] permissionIds,
                                    @RequestParam Boolean enable) {
        rolePermissionService.editBatch(roleId, permissionIds, enable);
        return Response.success();
    }
}
