package cn.icframework.system.module.chat;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.enums.ChatType;
import cn.icframework.system.enums.MsgType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 聊天记录
 * 多对多锻炼，可以是用户对用户也可以是群组
 */
@Getter
@Setter
@Table(value = "sys_chat", comment = "聊天")
public class Chat {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 最新的一条内容
     */
    @TableField(length = 500, comment = "内容")
    private String message;

    /**
     * 聊天名称，如果是空则会获取关联用户名
     */
    @TableField(length = 20, comment = "名称")
    private String name;

    /**
     * 头像文件
     */
    @TableField(comment = "头像文件", length = 255)
    private Long avatarFileId;

    /**
     * 头像文件地址
     */
    @TableField(comment = "头像文件地址", length = 500)
    private String avatarFileUrl;

    /**
     * 最新的一条概要
     */
    @TableField(length = 30, comment = "概要")
    private String summary;

    /**
     * 类型
     */
    @TableField(length = 4, comment = "类型")
    private MsgType msgType;

    /**
     * 聊天类型
     */
    @TableField(length = 4, notNull = true, comment = "聊天类型")
    private ChatType chatType;

    /**
     * 是否临时
     */
    @TableField(notNull = true, defaultValue = "false", comment = "是否临时")
    private Boolean temporary;

    /**
     * 所属人
     */
    @TableField(notNull = true, comment = "所属人id")
    private String ownerId;

    /**
     * 最新消息创建时间
     */
    @TableField(comment = "最新消息创建时间")
    private LocalDateTime lastMsgTime;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
