package cn.icframework.project.module.energy.swaporder.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.project.module.energy.swaporder.def.SwapOrderDef;
import org.springframework.stereotype.Component;

import java.util.List;

/** 换电订单查询条件构建器。 */
@Component
public class SwapOrderWrapperBuilder extends BasicWrapperBuilder<SwapOrderDef> {

    public SwapOrderWrapperBuilder() {
        super(SwapOrderDef.table());
    }

    @Override
    protected SqlWrapper list(QueryParams params, SwapOrderDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        params.forEach((key, requestValue) -> {
            Object value = requestValue.getValue();
            switch (key) {
                case "id" -> sqlWrapper.WHERE(def.id.eq(value));
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.orderNo.like(value));
                case "userId" -> sqlWrapper.WHERE(def.userId.eq(value));
                case "stationId" -> sqlWrapper.WHERE(def.stationId.eq(value));
                case "orderType" -> sqlWrapper.WHERE(def.orderType.eq(value));
                case "status" -> sqlWrapper.WHERE(def.status.eq(value));
                default -> {
                }
            }
        });
        return sqlWrapper;
    }

    @Override
    protected QueryField<?> doSort(OrderItem orderItem, SwapOrderDef def) {
        return switch (orderItem.getSortBy()) {
            case "amount" -> def.amount;
            case "startTime" -> def.startTime;
            case "endTime" -> def.endTime;
            case "updateTime" -> def.updateTime;
            default -> null;
        };
    }

    @Override
    protected List<DefaultOrderBy> defaultSort(SwapOrderDef def) {
        return new OrderBuilder().orderDesc(def.createTime).build();
    }
}
