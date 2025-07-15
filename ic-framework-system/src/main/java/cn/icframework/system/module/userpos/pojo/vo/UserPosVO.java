package cn.icframework.system.module.userpos.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/09/09
 */
@Getter
@Setter
public class UserPosVO {
    private Long id;
    /**
     * 职位id
     */
    private Long positionId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 姓名
     */
    private String userName;
}
