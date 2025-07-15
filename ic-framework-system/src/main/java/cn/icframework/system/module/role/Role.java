package cn.icframework.system.module.role;

import cn.icframework.mybatis.annotation.Index;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.common.enums.Status;
import cn.icframework.auth.entity.BasicRole;
import lombok.Getter;
import lombok.Setter;


/**
 * @Author hzl
 * @Date 2023/6/20 0020
 * @Description
 */
@Getter
@Setter
@Table(value = "sys_role", comment = "角色", indexes = {
        @Index(name = "idx_sign", columns = {"sign"}, unique = true),
        @Index(name = "idx_name", columns = {"name"}, unique = true),
})
public class Role extends BasicRole {
    public static Role def() {
        Role role = new Role();
        role.setStatus(Status.ENABLE);
        role.setSystem(false);
        return role;
    }
}
