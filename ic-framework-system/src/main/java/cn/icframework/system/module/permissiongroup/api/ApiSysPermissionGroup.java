package cn.icframework.system.module.permissiongroup.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.permissiongroup.pojo.dto.PermissionGroupDTO;
import cn.icframework.system.module.permissiongroup.pojo.vo.PermissionGroupVO;
import cn.icframework.system.module.permissiongroup.service.PermissionGroupService;
import cn.icframework.system.module.permissiongroup.wrapperbuilder.PermissionGroupWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/08/02
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/permission-group", name ="权限分组")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysPermissionGroup extends BasicApi {
    private final PermissionGroupService permissionGroupService;
    private final PermissionGroupWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/detail", name = "获取详情")
    public Response<PermissionGroupVO> detail(@RequestParam("id") Serializable id) {
        return Response.success(permissionGroupService.selectById(id, PermissionGroupVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<PermissionGroupVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return permissionGroupService.page(sqlWrapper, page, PermissionGroupVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @PostMapping(value = "/delete", name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        permissionGroupService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<PermissionGroupVO> all(String userType) {
        return permissionGroupService.all(userType);
    }

    /**
     * 编辑或者保存
     */
    @PostMapping(value = "/edit", name ="编辑")
    public Response<Void> edit(@Validated PermissionGroupDTO dto) {
        permissionGroupService.edit(dto);
        return Response.success();
    }
}
