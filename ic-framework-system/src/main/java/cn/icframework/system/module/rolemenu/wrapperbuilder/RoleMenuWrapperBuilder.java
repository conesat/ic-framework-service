package cn.icframework.system.module.rolemenu.wrapperbuilder;

import cn.icframework.common.enums.Status;
import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.menu.def.MenuDef;
import cn.icframework.system.module.rolemenu.def.RoleMenuDef;
import cn.icframework.system.module.userrole.def.UserRoleDef;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * wrapper构建器
 * @author ic
 * @date 2024/08/25
 */
@Component
public class RoleMenuWrapperBuilder extends BasicWrapperBuilder<RoleMenuDef> {

    /**
    * 返回一个数据库定义对象
    */
    public RoleMenuWrapperBuilder() {
        super(RoleMenuDef.table());
    }

    /**
     * 遍历查询条件，此方法会在AdminApi调用page时使用，你可以仿照page接口，用于其他接口查询
     * params 是前端传入的所有参数
     * 在这里根据参数和值 去构建SQLWrapper 并返回
     *
     * @param params 前端传入的所有参数
     * @param def    数据库映射
     * @return 构建的SQLWrapper条件
     */
    @Override
    protected SqlWrapper list(QueryParams params, RoleMenuDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                 case "roleId" -> sqlWrapper.WHERE(def.roleId.eq(value));
            }
        });
        return sqlWrapper;
    }

    /**
     * 获取角色菜单
     * @param roleIds
     * @param code
     * @return
     */
    public SqlWrapper roleMenuForTypeRole(Long userId, int code) {
        UserRoleDef userRoleDef = UserRoleDef.table();
        SqlWrapper sqlWrapper = SELECT(userRoleDef.roleId)
                .FROM(userRoleDef)
                .WHERE(userRoleDef.userId.eq(userId));

        RoleMenuDef roleMenuDef = RoleMenuDef.table();
        MenuDef menuDef = MenuDef.table();
        return SELECT(menuDef)
                .FROM(roleMenuDef)
                .LEFT_JOIN(menuDef).ON(menuDef.id.eq(roleMenuDef.menuId))
                .WHERE(roleMenuDef.roleId.in(sqlWrapper), menuDef.menuPlatformType.eq(code).status.eq(Status.ENABLE.code()));
    }

    /**
     * 获取平台类型菜单
     * @param code
     * @return
     */
    public SqlWrapper roleMenuForType(int code) {
        MenuDef menuDef = MenuDef.table();
        return SELECT(menuDef)
                .FROM(menuDef)
                .WHERE(menuDef.menuPlatformType.eq(code).status.eq(Status.ENABLE.code()));
    }

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicParamsHandler转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @param def       数据库映射
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, RoleMenuDef def) {
        return switch (orderItem.getSortBy()) {
            // case "createTime" -> def.createTime;
            default -> null;
        };
    }

    /**
     * 默认排序 当不满足doSort()时，会使用这里返回的内容进行排序
     *
     * @param def    数据库映射
     * @return 可以是多个排序条件的数组，按顺序进行排序
     */
    @Override
    protected List<DefaultOrderBy> defaultSort(RoleMenuDef def) {
         // 使用以下方法构建默认排序
         // return new OrderBuilder().orderDesc(def.createTime).build();
         return Collections.emptyList();
    }
}
