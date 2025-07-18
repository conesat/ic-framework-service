package cn.icframework.system.module.dic;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Index;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author iceFire
 * @since 2023/6/14
 */
@Getter
@Setter
@Index(name = "dic_idx", columns = {"dic_key"}, unique = true)
@Table(value = "sys_dic", comment = "字典")
public class Dic {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
     * 键
     */
    @TableField(notNull = true, comment = "键")
    private String dicKey;

    /**
     * 值
     */
    @TableField(notNull = true, comment = "值")
    private String dicVal;

    /**
     * 是否系统值
     */
    @TableField(defaultValue = "false", notNull = true, comment = "是否系统值")
    private Boolean system;

    /**
     * 状态
     */
    @TableField(defaultValue = "1", notNull = true, comment = "是否有效")
    private Status status;

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
