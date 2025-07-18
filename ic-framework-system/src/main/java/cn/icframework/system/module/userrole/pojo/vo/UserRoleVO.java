package cn.icframework.system.module.userrole.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic generator
 * @since 2023/08/09
 */
@Getter
@Setter
public class UserRoleVO {
    private String id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
