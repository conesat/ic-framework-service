package cn.icframework.system.module.rp.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.permission.def.PermissionDef;
import cn.icframework.system.module.role.def.RoleDef;
import cn.icframework.system.module.rp.def.RolePermissionDef;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


/**
 * @author ic generator
 * @since 2023/08/07
 */
@Component
public class RolePermissionWrapperBuilder extends BasicWrapperBuilder<RolePermissionDef> {
    public RolePermissionWrapperBuilder() {
        super(RolePermissionDef.table());
    }

    /**
     * 遍历查询条件，此方法会在AdminApi调用page时使用，你可以仿照page接口，用于其他接口查询
     * params 是前端传入的所有参数
     * 在这里根据参数和值 去构建SQLWrapper 并返回
     *
     * @param params 前端传入的所有参数
     * @return 构建的SQLWrapper条件
     */
    @Override
    protected SqlWrapper list(QueryParams params, RolePermissionDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        RolePermissionDef table = RolePermissionDef.table();
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case "roleId" -> sqlWrapper.WHERE(table.roleId.eq(value));
            }
        });
        return sqlWrapper;
    }

    /**
     * 根据角色id查询权限id
     * @param roleId
     * @return
     */
    public SqlWrapper allPermissionIdsByRoleId(Long roleId) {
        RoleDef roleDef = RoleDef.table();
        PermissionDef permissionDef = PermissionDef.table();
        RolePermissionDef rolePermissionDef = RolePermissionDef.table();
        return SELECT(rolePermissionDef.permissionId)
                .FROM(rolePermissionDef)
                .LEFT_JOIN(roleDef).ON(rolePermissionDef.roleId.eq(roleDef.id))
                .LEFT_JOIN(permissionDef).ON(rolePermissionDef.permissionId.eq(permissionDef.id))
                .WHERE(roleDef.id.eq(roleId), permissionDef.userType.eq(roleDef.userType));
    }

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicParamsHandler转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, RolePermissionDef def) {
        return switch (orderItem.getSortBy()) {
            default -> null;
        };
    }

    /**
     * 默认排序 当不满足doSort()时，会使用这里返回的内容进行排序
     *
     * @return 可以是多个排序条件的数组，按顺序进行排序
     */
    @Override
    protected List<DefaultOrderBy> defaultSort(RolePermissionDef def) {
        return Collections.emptyList();
    }

}
