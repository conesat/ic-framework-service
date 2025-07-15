package cn.icframework.system.module.userpos.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.userpos.def.UserPosDef;
import cn.icframework.system.module.userpos.pojo.vo.UserPosVO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * wrapper构建器
 * @author ic
 * @date 2024/09/09
 */
@Component
public class UserPosWrapperBuilder extends BasicWrapperBuilder<UserPosDef> {

    /**
     * 返回一个数据库定义对象
     */
    public UserPosWrapperBuilder() {
        super(UserPosDef.table());
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
    protected SqlWrapper list(QueryParams params, UserPosDef def) {
        UserDef userDef = UserDef.table();
        SqlWrapper sqlWrapper = SELECT(def._all, userDef.name.as(UserPosVO::getUserName)).FROM(def).LEFT_JOIN(userDef).ON(userDef.id.eq(def.userId));
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(userDef.name.like(value));
                case "posId" -> sqlWrapper.WHERE(def.positionId.eq(value));
            }
        });
        return sqlWrapper;
    }

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicParamsHandler转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @param def       数据库映射
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, UserPosDef def) {
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
    protected List<DefaultOrderBy> defaultSort(UserPosDef def) {
        // 使用以下方法构建默认排序
        // return new OrderBuilder().orderDesc(def.createTime).build();
        return Collections.emptyList();
    }
}
