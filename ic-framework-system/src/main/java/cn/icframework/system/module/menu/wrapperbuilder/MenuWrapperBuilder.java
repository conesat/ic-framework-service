package cn.icframework.system.module.menu.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.menu.def.MenuDef;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * wrapper构建器
 * @author ic
 * @since 2024/08/20
 */
@Component
public class MenuWrapperBuilder extends BasicWrapperBuilder<MenuDef> {

    /**
    * 返回一个数据库定义对象
    */
    public MenuWrapperBuilder() {
        super(MenuDef.table());
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
    protected SqlWrapper list(QueryParams params, MenuDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                 case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.name.like(value));
                 case "status" -> sqlWrapper.WHERE(def.status.eq(value));
                 case "userType" -> sqlWrapper.WHERE(def.userType.eq(value));
                 case "parentIsNull" -> sqlWrapper.WHERE(def.parentId.isNull());
                 case "menuPlatformType" -> sqlWrapper.WHERE(def.menuPlatformType.in(values));
            }
        });
        return sqlWrapper;
    }

    /**
     * 详情查询
     *
     * @param id 主键
     * @return 详情查询的SQLWrapper
     */
    public SqlWrapper detail(Serializable id) {
        MenuDef menuDef = MenuDef.table();
        MenuDef menuParentDef = MenuDef.table().as("parent");
        return SELECT(menuDef._all, menuParentDef.name.as("parentName"))
                .FROM(menuDef)
                .LEFT_JOIN(menuParentDef).ON(menuDef.parentId.eq(menuParentDef.id))
                .WHERE(menuDef.id.eq(id));
    }

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicParamsHandler转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @param def       数据库映射
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, MenuDef def) {
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
    protected List<DefaultOrderBy> defaultSort(MenuDef def) {
         // 使用以下方法构建默认排序
          return new OrderBuilder().orderAsc(def.orderNo).orderDesc(def.createTime).build();
    }
}
