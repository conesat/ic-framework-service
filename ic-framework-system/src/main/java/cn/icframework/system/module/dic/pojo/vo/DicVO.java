package cn.icframework.system.module.dic.pojo.vo;

import cn.icframework.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * @author create by ic gen
 * @date 2023/06/14
 */
@Getter
@Setter
public class DicVO {
    private Long id;
    private String dicKey;
    private String dicVal;
    private Status status;
    private Boolean system;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
