package cn.icframework.system.module.noticereceiver;

import cn.icframework.mybatis.annotation.Index;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Index(name = "notice_id_user_id_idx", columns = {"notice_id", "user_id"}, unique = true)
@Table(value = "sys_notice_receiver", comment = "通知接收对象")
public class NoticeReceiver {

    @TableField(value = "notice_id", comment = "通知id")
    private Long noticeId;

    @TableField(value = "user_id", comment = "用户id")
    private Long userId;

    @TableField(value = "hidden", notNull = true, defaultValue = "false", comment = "隐藏，不在列表显示")
    private Boolean hidden;

    @TableField(value = "read_time", comment = "查阅时间")
    private LocalDateTime readTime;

    public static NoticeReceiver def() {
        return new NoticeReceiver();
    }
}
