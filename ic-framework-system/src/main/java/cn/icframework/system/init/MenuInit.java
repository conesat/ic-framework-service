package cn.icframework.system.init;

import cn.icframework.system.consts.InitMd5Keys;
import cn.icframework.system.enums.IconType;
import cn.icframework.system.enums.MenuPlatformType;
import cn.icframework.system.enums.MenuType;
import cn.icframework.system.init.helper.InitHelper;
import cn.icframework.system.module.menu.Menu;
import cn.icframework.system.module.menu.def.MenuDef;
import cn.icframework.system.module.menu.pojo.dto.MenuInitDTO;
import cn.icframework.system.module.menu.service.MenuService;
import com.alibaba.fastjson.JSONArray;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 初始化菜单栏
 * @author hzl
 * @date 2024/9/4
 */
@Component
@RequiredArgsConstructor
public class MenuInit {

    private final MenuService menuService;
    private final InitHelper initHelper;

    public void initMenu() {
        try {
            List<MenuInitDTO> menuInitDTOS = new ArrayList<>();
            initHelper.processDic("/init/menu/", InitMd5Keys.MENU_INIT_MD5, json -> {
                menuInitDTOS.addAll(JSONArray.parseArray(json, MenuInitDTO.class));
            });
            if (!menuInitDTOS.isEmpty()) {
                List<String> systemPathList = new ArrayList<>();
                handlerMenu(menuInitDTOS, null, MenuPlatformType.SYS, systemPathList);
                // 有调整，已被移出的系统菜单删除掉
                menuService.delete(MenuDef.table().path.notIn(systemPathList).system.eq(true).menuPlatformType.eq(MenuPlatformType.SYS.code()), true, true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            menuService.removeAllCache();
        } catch (Exception ignored) {
        }
    }

    private void handlerMenu(List<MenuInitDTO> menuInitDTOS, Menu parent, MenuPlatformType platformType, List<String> systemPathList) {
        for (MenuInitDTO menuInitDTO : menuInitDTOS) {
            systemPathList.add(menuInitDTO.getPath());
            MenuDef menuDef = MenuDef.table();
            MenuType menuType = MenuType.LAYOUT;
            if (menuInitDTO.getMenuType() != null) {
                if (menuInitDTO.getMenuType().equalsIgnoreCase("IFRAME")) {
                    menuType = MenuType.IFRAME;
                } else if (menuInitDTO.getMenuType().equalsIgnoreCase("BLANK")) {
                    menuType = MenuType.BLANK;
                }
            }
            Menu menu = menuService.selectOne(menuDef.menuPlatformType.eq(MenuPlatformType.SYS).path.eq(menuInitDTO.getPath()));
            boolean update = false;
            if (menu == null) {
                menu = Menu.def();
            } else if (!Objects.equals(menu.getName(), menuInitDTO.getName()) ||
                    !Objects.equals(menu.getRedirect(), menuInitDTO.getRedirect()) ||
                    !Objects.equals(menu.getIcon(), menuInitDTO.getIcon()) ||
                    !Objects.equals(menu.getUrl(), menuInitDTO.getUrl()) ||
                    !Objects.equals(menu.getOrderNo(), menuInitDTO.getOrderNo()) ||
                    !Objects.equals(menu.getHidden(), menuInitDTO.getHidden()) ||
                    !Objects.equals(menu.getBelong(), menuInitDTO.getBelong()) ||
                    !Objects.equals(menu.getUserType(), menuInitDTO.getUserType()) ||
                    !Objects.equals(menu.getMenuType().code(), menuType.code()) ||
                    !Objects.equals(menu.getParentId(), parent == null ? null : parent.getId())
            ) {
                update = true;
            }
            menu.setMenuPlatformType(platformType);
            menu.setPath(menuInitDTO.getPath());
            menu.setName(menuInitDTO.getName());
            menu.setRedirect(menuInitDTO.getRedirect());
            if (menuInitDTO.getIcon() != null) {
                menu.setIcon(menuInitDTO.getIcon());
                menu.setIconType(IconType.instanceOf(menuInitDTO.getIconType()));
            }
            if (menuInitDTO.getOrderNo() != null) {
                menu.setOrderNo(menuInitDTO.getOrderNo());
            }
            menu.setUrl(menuInitDTO.getUrl());
            menu.setHidden(Objects.equals(menuInitDTO.getHidden(), true));
            menu.setMenuType(menuType);
            menu.setBelong(menuInitDTO.getBelong());
            menu.setSystem(true);
            if (parent != null) {
                menu.setParentId(parent.getId());
                menu.setUserType(parent.getUserType());
            } else {
                menu.setUserType(menuInitDTO.getUserType());
            }
            if (update) {
                menuService.updateById(menu);
            } else if (menu.getId() == null) {
                menuService.insert(menu);
            }
            if (menuInitDTO.getChildren() != null) {
                handlerMenu(menuInitDTO.getChildren(), menu, platformType, systemPathList);
            }
        }
    }

}
