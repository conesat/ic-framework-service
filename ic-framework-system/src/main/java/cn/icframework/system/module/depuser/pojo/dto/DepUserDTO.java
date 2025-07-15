package cn.icframework.system.module.depuser.pojo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/08/11
 */
@Getter
@Setter
public class DepUserDTO {
    private Long id;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 用户id
     */
    private Long userId;
}
