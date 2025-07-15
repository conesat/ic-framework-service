package cn.icframework.system.module.permission.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.permission.def.PermissionDef;
import cn.icframework.system.module.permissiongroup.def.PermissionGroupDef;
import cn.icframework.system.module.permissiongroup.pojo.vo.PermissionGroupVO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * @author iceFire
 * @date 2023/6/15
 */
@Component
public class PermissionWrapperBuilder extends BasicWrapperBuilder<PermissionDef> {

    public PermissionWrapperBuilder() {
        super(PermissionDef.table());
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
    protected SqlWrapper list(QueryParams params, PermissionDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.name.like(value));
                case "path" -> sqlWrapper.WHERE(def.path.eq(value));
                case "groupId" -> sqlWrapper.WHERE(def.groupId.eq(value));
                case "userType" -> sqlWrapper.WHERE(def.userType.like(value));
            }
        });
        return sqlWrapper;
    }

    /**
     * 获取所有权限
     *
     * @return
     */
    public SqlWrapper allWithGroup(String userType) {
        PermissionGroupDef permissionGroupDef = PermissionGroupDef.table();
        PermissionDef permissionDef = PermissionDef.table();
        permissionDef.as(PermissionGroupVO::getPermissionVOS);
        SqlWrapper sqlWrapper = SELECT(permissionGroupDef, permissionDef)
                .FROM(permissionGroupDef).LEFT_JOIN(permissionDef).ON(permissionGroupDef.id.eq(permissionDef.groupId));
        if (StringUtils.hasLength(userType)) {
            sqlWrapper.WHERE(permissionDef.userType.eq(userType));
        }
        return sqlWrapper;
    }

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicWrapperBuilder转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, PermissionDef def) {
        return switch (orderItem.getSortBy()) {
            case "name" -> def.name;
            default -> null;
        };
    }

    /**
     * 默认排序 当不满足doSort时，会使用这里返回的内容进行排序
     *
     * @return 可以是多个排序条件的数组，按顺序进行排序
     */
    @Override
    protected List<DefaultOrderBy> defaultSort(PermissionDef def) {
        return new OrderBuilder().orderDesc(def.path).build();
    }

}
