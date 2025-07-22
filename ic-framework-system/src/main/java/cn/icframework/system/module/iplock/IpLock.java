package cn.icframework.system.module.iplock;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "ip_lock", comment = "ip登录锁")
public class IpLock {
    /**
     * 最大登录失败次数
     */
    public static final int MAX_LOGIN_FAIL_COUNT = 5;
    /**
     * 锁定时间
     */
    public static final int LOCK_MINUTES = 10;

    @Id(idType = IdType.INPUT)
    private String ip;

    /**
     * 登陆失败次数
     */
    @TableField(comment = "登陆失败次数")
    private int loginFailCount;

    /**
     * 首次失败时间
     */
    @TableField(notNull = true, comment = "首次失败时间", onInsertValue = "now()")
    private LocalDateTime firstFailTime;

    /**
     * 锁定结束时间
     */
    @TableField(comment = "锁定结束时间")
    private LocalDateTime lockEndTime;
}
