package cn.icframework.system.module.role.pojo.vo;

import cn.icframework.common.enums.Status;
import cn.icframework.system.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author create by ic gen
 * @date 2023/06/20
 */
@Getter
@Setter
public class RoleVO {
    private Long id;
    /**
     * 状态
     */
    private Status status;
    private String userType;
    private String userTypeText;
    private boolean system;
    /**
     * 名称
     */
    private String name;
    private String sign;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public String getUserTypeText() {
        return UserType.instanceOf(userType).text();
    }
}
