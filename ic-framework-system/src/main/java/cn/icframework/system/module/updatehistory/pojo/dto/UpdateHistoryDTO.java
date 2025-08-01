package cn.icframework.system.module.updatehistory.pojo.dto;

import java.lang.Integer;
import java.time.LocalDateTime;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2025/07/31
 */
@Getter
@Setter
public class UpdateHistoryDTO {
    /**
     * 更新备注
     */
    private String remark;
    /**
     * 更新人姓名
     */
    private String personName;
    /**
     * 更新包文件id
     */
    @NotNull
    private Long fileId;
}
