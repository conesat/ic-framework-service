package cn.icframework.system.module.depuser.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/08/11
 */
@Getter
@Setter
public class DepUserVO {
    private Long id;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 是否负责人
     */
    private Boolean manager;
    /**
     * 用户名
     */
    private String username;
    /**
     * 头像
     */
    private String avatarFileUrl;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
