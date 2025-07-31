package cn.icframework.system.module.updatehistory;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.system.module.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "update_history", comment = "更新历史")
public class UpdateHistory {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 名称
     */
    @TableField(comment = "名称")
    private String name;

    /**
     * 更新版本
     */
    @TableField(comment = "更新版本")
    private Integer versionNumber;

    /**
     * 更新版本号
     */
    @TableField(comment = "更新版本号")
    private String version;

    /**
     * 更新前版本号
     */
    @TableField(comment = "更新前版本号")
    private String beforeVersion;

    /**
     * 更新备注
     */
    @TableField(comment = "更新备注")
    private String remark;

    /**
     * 更新人姓名
     */
    @TableField(comment = "更新人姓名")
    private String personName;

    /**
     * 更新人id
     */
    @ForeignKey(references = User.class)
    @TableField(comment = "更新人id")
    private String userId;

    /**
     * 创建时间
     */
    @TableField(notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;
}
