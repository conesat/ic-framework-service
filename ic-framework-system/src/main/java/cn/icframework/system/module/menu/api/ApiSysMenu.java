package cn.icframework.system.module.menu.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.Response;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.menu.pojo.dto.MenuDTO;
import cn.icframework.system.module.menu.pojo.vo.MenuVO;
import cn.icframework.system.module.menu.pojo.vo.MenuWithChildrenVO;
import cn.icframework.system.module.menu.service.MenuService;
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
 * @date 2024/08/20
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/menu", name ="菜单")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysMenu extends BasicApi {
    private final MenuService menuService;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<MenuVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(menuService.detail(id));
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<MenuWithChildrenVO> all(@RequestParam(value = "menuPlatformType", required = false) Integer[] menuPlatformType,
                                        @RequestParam(value = "userType", required = false) String userType) {
        return menuService.allWithCache(menuPlatformType, userType);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        menuService.deleteByIds(ids);
        menuService.removeAllCache();
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated MenuDTO dto) {
        menuService.edit(dto);
        menuService.removeAllCache();
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated MenuDTO dto) {
        menuService.edit(dto);
        menuService.removeAllCache();
        return Response.success();
    }


}
