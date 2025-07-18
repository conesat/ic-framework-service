package cn.icframework.system.module.menu;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Index;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.common.enums.Status;
import cn.icframework.system.enums.IconType;
import cn.icframework.system.enums.MenuPlatformType;
import cn.icframework.system.enums.MenuType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Index(name = "path_idx", columns = {"menu_platform_type", "path"}, unique = true)
@Table(value = "sys_menu", comment = "菜单")
public class Menu implements Serializable {
    @Serial
    private static final long serialVersionUID = -3093875725432550786L;

    @Id(idType = IdType.AUTO)
    private Long id;

    /**
     * 菜单名
     */
    @TableField(length = 10, comment = "菜单名")
    private String name;

    /**
     * 上级菜单id
     */
    @TableField(comment = "上级菜单id")
    private Long parentId;

    /**
     * icon名
     */
    @TableField(length = 50, comment = "icon名")
    private String icon;

    /**
     * icon类型
     */
    @TableField(comment = "icon类型")
    private IconType iconType;

    /**
     * 类型
     */
    @TableField(comment = "类型")
    private MenuType menuType;

    /**
     * 归属平台类型
     */
    @TableField(comment = "归属平台类型")
    private MenuPlatformType menuPlatformType;

    /**
     * 允许分配的用户类型
     */
    @TableField(notNull = true, comment = "允许分配的用户类型")
    private String userType;

    /**
     * 是与否有效
     */
    @TableField(defaultValue = "1", comment = "是与否有效")
    private Status status;

    /**
     * 排序号
     */
    @TableField(defaultValue = "0", comment = "排序号")
    private Integer orderNo;

    /**
     * 路径
     */
    @TableField(comment = "路径")
    private String path;

    /**
     * 重定向，访问当前路径会重定向到这个地址
     */
    @TableField(comment = "重定向")
    private String redirect;

    /**
     * 如果是外部
     * 则是地址
     * 否则是文件路径
     */
    @TableField(comment = "地址")
    private String url;

    /**
     * 是否隐藏
     */
    @TableField(defaultValue = "false",comment = "是否隐藏")
    private Boolean hidden;

    /**
     * 是否保持状态
     */
    @TableField(defaultValue = "false", comment = "是否保持状态")
    private Boolean keepAlive;

    /**
     * 层级路径，有系统生成，用于判断层级
     */
    @TableField(comment = "层级路径")
    private String parentPath;

    /**
     * 隶属,访问该菜单，会让belong的菜单高亮
     */
    @TableField(comment = "隶属")
    private String belong;

    /**
     * 是否系统
     */
    @TableField(defaultValue = "false", comment = "是否系统")
    private Boolean system;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(comment = "更新时间", onUpdateValue = "now()")
    private LocalDateTime updateTime;
}
