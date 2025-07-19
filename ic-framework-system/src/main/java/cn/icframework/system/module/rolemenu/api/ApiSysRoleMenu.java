package cn.icframework.system.module.rolemenu.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.rolemenu.pojo.vo.RoleMenuVO;
import cn.icframework.system.module.rolemenu.service.RoleMenuService;
import cn.icframework.system.module.rolemenu.wrapperbuilder.RoleMenuWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * @since 2024/08/25
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/role-menu", name ="角色菜单")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysRoleMenu extends BasicApi {
    private final RoleMenuService roleMenuService;
    private final RoleMenuWrapperBuilder wrapperBuilder;

    /**
     * 查询全部
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<RoleMenuVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return roleMenuService.select(sqlWrapper, RoleMenuVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        roleMenuService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 批量编辑
     */
    @PostMapping(value = "edit-batch",name ="批量编辑")
    public Response<Void> editBatch(@RequestParam("roleId") Long roleId, @RequestParam("menuIds") Long[] menuIds) {
        roleMenuService.editBatch(roleId, menuIds);
        return Response.success();
    }
}
