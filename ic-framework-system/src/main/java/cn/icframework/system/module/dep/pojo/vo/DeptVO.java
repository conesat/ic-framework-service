package cn.icframework.system.module.dep.pojo.vo;

import cn.icframework.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author create by ic gen
 * @since 2023/06/21
 */
@Getter
@Setter
public class DeptVO {
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 完整id路径
     */
    private String parentPath;
    private Integer parentId;
    private String parentName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 排序
     */
    private int sort;
    /**
     * 负责人
     */
    private Long leaderUserId;
    private String leaderUserName;
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 状态
     */
    private Status status;

}
