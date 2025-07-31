package cn.icframework.system.module.updatehistory.pojo.dto;

import java.lang.Integer;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2025/07/31
 */
@Getter
@Setter
public class UpdateHistoryDTO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 更新版本
     */
    private Integer versionNumber;
    /**
     * 更新版本号
     */
    private String version;
    /**
     * 更新前版本号
     */
    private String beforeVersion;
    /**
     * 更新备注
     */
    private String remark;
    /**
     * 更新人姓名
     */
    private String personName;
    /**
     * 更新人id
     */
    private String userId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
