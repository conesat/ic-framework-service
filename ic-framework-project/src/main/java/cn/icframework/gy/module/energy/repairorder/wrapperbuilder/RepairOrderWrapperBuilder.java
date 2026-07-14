package cn.icframework.project.module.energy.repairorder.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.project.module.energy.repairorder.def.RepairOrderDef;
import org.springframework.stereotype.Component;

import java.util.List;

/** 维修工单查询条件构建器。 */
@Component
public class RepairOrderWrapperBuilder extends BasicWrapperBuilder<RepairOrderDef> {

    public RepairOrderWrapperBuilder() {
        super(RepairOrderDef.table());
    }

    @Override
    protected SqlWrapper list(QueryParams params, RepairOrderDef def) {
        SqlWrapper sqlWrapper = SELECT_FROM(def);
        params.forEach((key, requestValue) -> {
            Object value = requestValue.getValue();
            switch (key) {
                case "id" -> sqlWrapper.WHERE(def.id.eq(value));
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.orderNo.like(value), OR(), def.repairType.like(value), OR(), def.contactPhone.like(value));
                case "userId" -> sqlWrapper.WHERE(def.userId.eq(value));
                case "stationId" -> sqlWrapper.WHERE(def.stationId.eq(value));
                case "status" -> sqlWrapper.WHERE(def.status.eq(value));
                default -> {
                }
            }
        });
        return sqlWrapper;
    }

    @Override
    protected QueryField<?> doSort(OrderItem orderItem, RepairOrderDef def) {
        return switch (orderItem.getSortBy()) {
            case "repairTime" -> def.repairTime;
            case "createTime" -> def.createTime;
            case "updateTime" -> def.updateTime;
            default -> null;
        };
    }

    @Override
    protected List<DefaultOrderBy> defaultSort(RepairOrderDef def) {
        return new OrderBuilder().orderDesc(def.createTime).build();
    }
}
