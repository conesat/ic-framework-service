package cn.icframework.system.module.menu.pojo.dto;

import cn.icframework.common.enums.Status;
import cn.icframework.system.enums.IconType;
import cn.icframework.system.enums.MenuPlatformType;
import cn.icframework.system.enums.MenuType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/08/20
 */
@Getter
@Setter
public class MenuDTO {
    private Long id;
    /**
     * 菜单名
     */
    @NotEmpty
    private String name;
    /**
     * 上级菜单id
     */
    private Long parentId;
    /**
     * icon名
     */
    private String icon;
    /**
     * 图标类型
     */
    private IconType iconType;
    /**
     * 类型
     */
    @NotNull
    private MenuType menuType;
    /**
     * 归属平台类型
     */
    @NotNull
    private MenuPlatformType menuPlatformType;
    /**
     * 是与否有效
     */
    @NotNull
    private Status status;
    /**
     * 允许分配的用户类型
     */
    @NotNull
    private String userType;
    /**
     * 排序号
     */
    @NotNull
    private Integer orderNo;
    /**
     * 路径
     */
    private String url;
    /**
     * 路径
     */
    @NotEmpty
    private String path;
    /**
     * 隐藏
     */
    private Boolean hidden;
    /**
     * 是否保持状态
     */
    private Boolean keepAlive;
}
