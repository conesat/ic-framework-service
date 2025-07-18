package cn.icframework.system.module.menu.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.system.module.menu.Menu;
import org.springframework.stereotype.Component;

/**
 * @author ic
 * @since 2024/08/20
 */
@Component
public class MenuVOConverter extends BasicConverter<Menu, MenuWithChildrenVO> {
}
