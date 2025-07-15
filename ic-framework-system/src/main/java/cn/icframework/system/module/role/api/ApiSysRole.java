package cn.icframework.system.module.role.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.role.pojo.dto.RoleDTO;
import cn.icframework.system.module.role.pojo.vo.RoleVO;
import cn.icframework.system.module.role.service.RoleService;
import cn.icframework.system.module.role.wrapperbuilder.RoleWrapperBuilder;
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
 * @date 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/role", name = "角色")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysRole extends BasicApi {
    private final RoleService roleService;
    private final RoleWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<RoleVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(roleService.selectById(id, RoleVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<RoleVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return roleService.page(sqlWrapper, page, RoleVO.class);
    }


    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        roleService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated RoleDTO dto) {
        roleService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated RoleDTO dto) {
        roleService.edit(dto);
        return Response.success();
    }
}
