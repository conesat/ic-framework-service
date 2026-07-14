package cn.icframework.project.module.energy.swapstation.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.project.module.energy.swapstation.def.SwapStationDef;
import org.springframework.stereotype.Component;

import java.util.List;

/** 换电站查询条件构建器。 */
@Component
public class SwapStationWrapperBuilder extends BasicWrapperBuilder<SwapStationDef> {

    public SwapStationWrapperBuilder() {
        super(SwapStationDef.table());
    }

    @Override
    protected SqlWrapper list(QueryParams params, SwapStationDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        params.forEach((key, requestValue) -> {
            Object value = requestValue.getValue();
            switch (key) {
                case "id" -> sqlWrapper.WHERE(def.id.eq(value));
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(
                    def.name.like(value), OR(), def.stationCode.like(value), OR(), def.city.like(value), OR(),
                    def.district.like(value), OR(), def.address.like(value)
                );
                case "status" -> sqlWrapper.WHERE(def.status.eq(value));
                default -> {
                }
            }
        });
        return sqlWrapper;
    }

    @Override
    protected QueryField<?> doSort(OrderItem orderItem, SwapStationDef def) {
        return switch (orderItem.getSortBy()) {
            case "name" -> def.name;
            case "availableBatteryCount" -> def.availableBatteryCount;
            case "rating" -> def.rating;
            case "updateTime" -> def.updateTime;
            default -> null;
        };
    }

    @Override
    protected List<DefaultOrderBy> defaultSort(SwapStationDef def) {
        return new OrderBuilder().orderDesc(def.updateTime).build();
    }
}
