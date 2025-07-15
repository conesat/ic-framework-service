package cn.icframework.system.module.notice;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.common.enums.Status;
import cn.icframework.system.enums.NoticeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "sys_notice", comment = "通知")
public class Notice {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title", notNull = true, length = 50, comment = "标题")
    private String title;

    /**
     * 内容
     */
    @TableField(value = "content", type = "text", comment = "内容")
    private String content;

    /**
     * 是与否有效
     */
    @TableField(value = "status", notNull = true, comment = "是与否有效")
    private Status status;

    /**
     * 类型
     */
    @TableField(value = "notice_type", notNull = true, comment = "类型")
    private NoticeType noticeType;

    /**
     * 归属人
     */
    @TableField(value = "user_id", notNull = true, comment = "归属人")
    private Long userId;

    /**
     * 是否面向所有人
     */
    @TableField(value = "to_all", notNull = true, defaultValue = "false", comment = "是否面向所有人")
    private boolean toAll;

    /**
     * 有效开始时间
     */
    @TableField(value = "enable_time", comment = "有效开始时间")
    private LocalDateTime enableTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", comment = "更新时间", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    public static Notice def() {
        Notice notice = new Notice();
        notice.setStatus(Status.ENABLE);
        return notice;
    }
}
