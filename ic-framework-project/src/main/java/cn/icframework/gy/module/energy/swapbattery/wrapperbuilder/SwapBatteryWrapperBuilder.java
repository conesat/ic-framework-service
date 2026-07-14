package cn.icframework.project.module.energy.swapbattery.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.project.module.energy.swapbattery.def.SwapBatteryDef;
import org.springframework.stereotype.Component;

import java.util.List;

/** 换电电池查询条件构建器。 */
@Component
public class SwapBatteryWrapperBuilder extends BasicWrapperBuilder<SwapBatteryDef> {

    public SwapBatteryWrapperBuilder() {
        super(SwapBatteryDef.table());
    }

    @Override
    protected SqlWrapper list(QueryParams params, SwapBatteryDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        params.forEach((key, requestValue) -> {
            Object value = requestValue.getValue();
            switch (key) {
                case "id" -> sqlWrapper.WHERE(def.id.eq(value));
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.batteryNo.like(value), OR(), def.model.like(value));
                case "stationId" -> sqlWrapper.WHERE(def.stationId.eq(value));
                case "status" -> sqlWrapper.WHERE(def.status.eq(value));
                default -> {
                }
            }
        });
        return sqlWrapper;
    }

    @Override
    protected QueryField<?> doSort(OrderItem orderItem, SwapBatteryDef def) {
        return switch (orderItem.getSortBy()) {
            case "powerPercent" -> def.powerPercent;
            case "healthPercent" -> def.healthPercent;
            case "lastSwapTime" -> def.lastSwapTime;
            case "updateTime" -> def.updateTime;
            default -> null;
        };
    }

    @Override
    protected List<DefaultOrderBy> defaultSort(SwapBatteryDef def) {
        return new OrderBuilder().orderDesc(def.updateTime).build();
    }
}
