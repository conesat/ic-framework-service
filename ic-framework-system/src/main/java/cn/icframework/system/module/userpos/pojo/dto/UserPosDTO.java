package cn.icframework.system.module.userpos.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/09/09
 */
@Getter
@Setter
public class UserPosDTO {
    private Long id;
    /**
     * 职位id
     */
    private Long positionId;
    /**
     * 用户id
     */
    private Long userId;

}
