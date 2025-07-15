package cn.icframework.system.module.dep.pojo.dto;

import cn.icframework.common.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author create by ic gen
 * @date 2023/06/21
 */
@Getter
@Setter
public class DeptDTO {
    private Long id;
    /**
     * 部门名称
     */
    @NotEmpty
    private String name;
    /**
     * 完整id路径
     */
    private String parentPath;
    private Long parentId;
    private String parentName;
    /**
     * 排序
     */
    private int sort;
    /**
     * 负责人
     */
    private Long leaderUserId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 状态
     */
    private Status status;
}
