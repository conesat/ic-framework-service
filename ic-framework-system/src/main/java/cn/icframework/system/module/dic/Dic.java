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
 * @date 2023/6/14
 */
@Getter
@Setter
@Index(name = "dic_idx", columns = {"dic_key"}, unique = true)
@Table(value = "sys_dic", comment = "字典")
public class Dic {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    @TableField(value = "dic_key", notNull = true, comment = "键")
    private String dicKey;

    @TableField(value = "dic_val", notNull = true, comment = "值")
    private String dicVal;

    @TableField(value = "system", notNull = true, comment = "是否系统值")
    private Boolean system;

    @TableField(value = "status", notNull = true, comment = "是否有效")
    private Status status;

    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    @TableField(value = "update_time", comment = "更新时间", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    public static Dic def() {
        Dic dic = new Dic();
        dic.setSystem(false);
        dic.setStatus(Status.ENABLE);
        return dic;
    }
}
