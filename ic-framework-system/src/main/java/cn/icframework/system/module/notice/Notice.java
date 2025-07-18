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
    @TableField(notNull = true, length = 50, comment = "标题")
    private String title;

    /**
     * 内容
     */
    @TableField(type = "text", comment = "内容")
    private String content;

    /**
     * 是与否有效
     */
    @TableField(defaultValue = "1", notNull = true, comment = "是与否有效")
    private Status status;

    /**
     * 类型
     */
    @TableField(notNull = true, comment = "类型")
    private NoticeType noticeType;

    /**
     * 归属人
     */
    @TableField(notNull = true, comment = "归属人")
    private Long userId;

    /**
     * 是否面向所有人
     */
    @TableField(notNull = true, defaultValue = "false", comment = "是否面向所有人")
    private boolean toAll;

    /**
     * 有效开始时间
     */
    @TableField(comment = "有效开始时间")
    private LocalDateTime enableTime;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(comment = "更新时间", onUpdateValue = "now()")
    private LocalDateTime updateTime;
}
