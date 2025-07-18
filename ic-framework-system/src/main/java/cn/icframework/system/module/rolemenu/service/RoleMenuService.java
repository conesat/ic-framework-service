package cn.icframework.system.module.rolemenu.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.system.enums.MenuPlatformType;
import cn.icframework.system.module.menu.pojo.vo.MenuWithChildrenVO;
import cn.icframework.system.module.rolemenu.RoleMenu;
import cn.icframework.system.module.rolemenu.dao.RoleMenuMapper;
import cn.icframework.system.module.rolemenu.def.RoleMenuDef;
import cn.icframework.system.module.rolemenu.wrapperbuilder.RoleMenuWrapperBuilder;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ic
 * @since 2024/08/25
 */
@AllArgsConstructor
@Service
public class RoleMenuService extends BasicService<RoleMenuMapper, RoleMenu> {
    private final RoleMenuWrapperBuilder wrapperBuilder;

    /**
     * 编辑或者保存
     */
    @Transactional
    public void editBatch(Long roleId, Long[] menuIds) {
        RoleMenuDef roleMenuDef = RoleMenuDef.table();
        delete(roleMenuDef.roleId.eq(roleId));
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu menu = new RoleMenu();
            menu.setRoleId(roleId);
            menu.setMenuId(menuId);
            roleMenus.add(menu);
        }
        insertBatch(roleMenus);
    }

    /**
     * 获取角色菜单
     *
     * @param menuPlatformType 平台类型
     * @return 菜单列表
     */
    @Cacheable(cacheNames = "menu", key = "#menuPlatformType.code()")
    public List<MenuWithChildrenVO> getMenu(MenuPlatformType menuPlatformType) {
        return select(wrapperBuilder.roleMenuForType(menuPlatformType.code()), MenuWithChildrenVO.class);
    }

    public List<MenuWithChildrenVO> getMenu(Long userId, MenuPlatformType menuPlatformType) {
        return select(wrapperBuilder.roleMenuForTypeRole(userId, menuPlatformType.code()), MenuWithChildrenVO.class);
    }
}
