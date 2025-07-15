package cn.icframework.system.module.menu.pojo.dto;

import cn.icframework.system.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author hzl
 * @date 2024/8/27
 */
@Getter
@Setter
public class MenuInitDTO {
    /**
     * 菜单名
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 重定向
     */
    private String redirect;
    /**
     * 隶属
     */
    private String belong;
    /**
     * 隐藏
     */
    private Boolean hidden;
    /**
     * 图标
     */
    private String icon;
    /**
     * 上级
     */
    private String parent;
    /**
     * 图标类型
     */
    private Integer iconType;
    /**
     * 图标类型
     */
    private Integer orderNo;
    /**
     * 外部地址
     */
    private String url;
    /**
     * 菜单类型
     *
     * @see cn.icframework.system.enums.MenuType
     */
    private String menuType;
    /**
     * 允许分配的用户类型
     *
     * @see UserType
     */
    private String userType;
    /**
     * 子菜单
     */
    private List<MenuInitDTO> children;
}
