package cn.icframework.system.module.user.pojo.vo;

import cn.icframework.mybatis.annotation.Join;
import cn.icframework.mybatis.annotation.Joins;
import cn.icframework.system.module.dep.Dept;
import cn.icframework.system.module.dep.pojo.vo.DeptSimpleVO;
import cn.icframework.system.module.depuser.DepUser;
import cn.icframework.system.module.position.Position;
import cn.icframework.system.module.position.pojo.vo.PositionSimpleVO;
import cn.icframework.system.module.role.Role;
import cn.icframework.system.module.role.pojo.vo.RoleSimpleVO;
import cn.icframework.system.module.userpos.UserPos;
import cn.icframework.system.module.userrole.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author hzl
 * @date 2023/5/31
 */
@Getter
@Setter
public class UserDetailVO extends UserVO {

    /**
     * 所属角色
     */
    @Joins(joins = {
            @Join(joinTable = UserRole.class, selfField = "id", joinTableField = "userId"),
            @Join(joinTable = Role.class, selfField = "roleId", joinTableField = "id")
    })
    private List<RoleSimpleVO> roles;

    /**
     * 所属部门
     */
    @Joins(joins = {
            @Join(joinTable = DepUser.class, selfField = "id", joinTableField = "userId"),
            @Join(joinTable = Dept.class, selfField = "depId", joinTableField = "id")
    })
    private List<DeptSimpleVO> deps;

    /**
     * 岗位
     */
    @Joins(joins = {
            @Join(joinTable = UserPos.class, selfField = "id", joinTableField = "userId"),
            @Join(joinTable = Position.class, selfField = "positionId", joinTableField = "id")
    })
    private List<PositionSimpleVO> pos;

}
