package cn.icframework.system.module.setting.pojo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author iceFire
 * @since 2023/6/8
 */
@Getter
@Setter
public class InitDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String url;
    @NotEmpty
    private String activationCode;
    @NotEmpty
    private String username;
    @NotEmpty
    private String passwd;
}
