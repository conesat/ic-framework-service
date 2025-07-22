package cn.icframework.system.module.iplock.pojo.vo;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @since 2025/07/22
 */
@Getter
@Setter
public class IpLockVO {
    private String ip;
    /**
     * 登陆失败次数
     */
    private int loginFailCount;
    /**
     * 首次失败时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstFailTime;
    /**
     * 锁定结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lockEndTime;

}
