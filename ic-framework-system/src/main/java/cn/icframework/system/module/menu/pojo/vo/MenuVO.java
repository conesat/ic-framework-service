package cn.icframework.system.module.menu.pojo.vo;

import cn.icframework.common.enums.Status;
import cn.icframework.system.enums.IconType;
import cn.icframework.system.enums.MenuPlatformType;
import cn.icframework.system.enums.MenuType;
import cn.icframework.system.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/08/20
 */
@Getter
@Setter
public class MenuVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 菜单名
     */
    private String name;
    /**
     * 上级菜单id
     */
    private Long parentId;
    private String parentName;
    /**
     * icon名
     */
    private String icon;
    private IconType iconType;
    /**
     * 类型
     */
    private MenuType menuType;
    /**
     * 归属平台类型
     */
    private MenuPlatformType menuPlatformType;
    /**
     * 是与否有效
     */
    private Status status;
    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 路径
     */
    private String url;
    /**
     * 路径
     */
    private String path;

    /**
     * 是否系统
     */
    private Boolean system;
    /**
     * 隐藏
     */
    private Boolean hidden;
    /**
     * 隶属
     */
    private String belong;

    private String userType;
    private String userTypeText;

    /**
     * 是否保持状态
     */
    private Boolean keepAlive;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public String getUserTypeText() {
        return UserType.instanceOf(userType).text();
    }
}
