package cn.icframework.system.module.dic.pojo.dto;

import cn.icframework.common.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author create by ic gen
 * @date 2023/06/14
 */
@Getter
@Setter
public class DicDTO {
    private Long id;
    @NotNull
    private Status status;
    @NotEmpty
    private String dicKey;
    private String dicVal;
    private LocalDateTime createTime;
}
