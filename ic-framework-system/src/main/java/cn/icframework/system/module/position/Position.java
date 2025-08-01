package cn.icframework.system.module.position;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Index;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Index(name = "sign_idx", columns = "sign", unique = true)
@Table(value = "sys_position", comment = "职位")
public class Position {

    @Id(idType = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(length = 10, comment = "名称")
    private String name;

    /**
     * 编码
     */
    @TableField(length = 50, comment = "编码")
    private String sign;

    /**
     * 职级
     */
    @TableField(length = 3, comment = "职级")
    private Integer level;

    /**
     * 是与否有效
     */
    @TableField(comment = "是与否有效")
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
