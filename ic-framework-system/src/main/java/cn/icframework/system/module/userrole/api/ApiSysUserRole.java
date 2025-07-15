package cn.icframework.system.module.userrole.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.userrole.pojo.dto.UserRoleDTO;
import cn.icframework.system.module.userrole.pojo.vo.UserRoleVO;
import cn.icframework.system.module.userrole.service.UserRoleService;
import cn.icframework.system.module.userrole.wrapperbuilder.UserRoleWrapperBuilder;
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
 * admin接口，用于管理后台
 * @RequireAuth(UserType.R_ADMIN) 指定这类用户才能访问
 *
 * @author ic generator
 * @date 2023/08/09
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/user-role", name ="用户权限")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysUserRole extends BasicApi {
    private final UserRoleService userRoleService;
    private final UserRoleWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<UserRoleVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(userRoleService.selectById(id, UserRoleVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<UserRoleVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return userRoleService.page(sqlWrapper, page, UserRoleVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public Response<List<UserRoleVO>> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return Response.success(userRoleService.select(sqlWrapper, UserRoleVO.class));
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        userRoleService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑或者保存
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated UserRoleDTO dto) {
        userRoleService.edit(dto);
        return Response.success();
    }
}
