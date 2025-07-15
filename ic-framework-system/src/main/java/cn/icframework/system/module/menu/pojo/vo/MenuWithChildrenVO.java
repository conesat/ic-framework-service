package cn.icframework.system.module.menu.pojo.vo;

import cn.icframework.mybatis.annotation.Join;
import cn.icframework.system.module.menu.Menu;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author ic
 * @date 2024/08/20
 */
@Getter
@Setter
public class MenuWithChildrenVO extends MenuVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 下级
     */
    @Join(joinTable = Menu.class, joinTableField = "parentId", selfField = "id")
    private List<MenuWithChildrenVO> children;

    public List<MenuWithChildrenVO> getChildren() {
        return children == null ? Collections.emptyList() : children;
    }
}
