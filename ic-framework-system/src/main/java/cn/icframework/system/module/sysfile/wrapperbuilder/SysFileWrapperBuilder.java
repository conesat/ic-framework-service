package cn.icframework.system.module.sysfile.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.sysfile.def.SysFileDef;
import cn.icframework.system.module.sysfile.pojo.vo.SysFileVO;
import cn.icframework.system.module.user.def.UserDef;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author hzl
 * @date 2023/5/12
 */
@Component
public class SysFileWrapperBuilder extends BasicWrapperBuilder<SysFileDef> {
    public SysFileWrapperBuilder() {
        super(SysFileDef.table());
    }

    @Override
    protected SqlWrapper list(QueryParams params, SysFileDef def) {
        UserDef userDef = UserDef.table();
        SqlWrapper sqlWrapper = SELECT(def._all, userDef.name.as(SysFileVO::getUserName))
                .FROM(def)
                .LEFT_JOIN(userDef).ON(def.userId.eq(userDef.id));
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.name.like(value), OR(), userDef.name.like(value));
            }
        });
        return sqlWrapper;
    }

    @Override
    protected QueryField<?> doSort(OrderItem orderItem, SysFileDef def) {
        return switch (orderItem.getSortBy()) {
            case "createTime" -> def.createTime;
            default -> null;
        };
    }

    @Override
    protected List<DefaultOrderBy> defaultSort(SysFileDef def) {
        return new OrderBuilder().orderDesc(def.createTime).build();
    }

}
