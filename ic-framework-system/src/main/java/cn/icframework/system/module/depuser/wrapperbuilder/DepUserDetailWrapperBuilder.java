package cn.icframework.system.module.depuser.wrapperbuilder;

import cn.icframework.common.lambda.LambdaUtils;
import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.wrapper.FunctionWrapper;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.depuser.def.DepUserDef;
import cn.icframework.system.module.depuser.pojo.vo.DepUserDetailVO;
import cn.icframework.system.module.depuser.pojo.vo.DepUserVO;
import cn.icframework.system.module.position.def.PositionDef;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.userpos.def.UserPosDef;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.icframework.mybatis.wrapper.FunctionWrapper.ANY_VALUE;
import static cn.icframework.mybatis.wrapper.FunctionWrapper.GROUP_CONCAT;
import static cn.icframework.mybatis.wrapper.FunctionWrapper.MAX;


/**
 * @author ic
 * @date 2024/08/11
 */
@Component
public class DepUserDetailWrapperBuilder extends BasicWrapperBuilder<DepUserDef> {

    /**
     * 返回一个数据库定义对象
     */
    public DepUserDetailWrapperBuilder() {
        super(DepUserDef.table());
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
    protected SqlWrapper list(QueryParams params, DepUserDef def) {
        PositionDef positionDef = PositionDef.table();
        UserPosDef userPosDef = UserPosDef.table();
        UserDef userDef = UserDef.table();
        SqlWrapper sqlWrapper = SELECT(
                ANY_VALUE(def.id).as(DepUserVO::getId),
                ANY_VALUE(userDef.name).as(DepUserVO::getName),
                ANY_VALUE(userDef.username).as(DepUserVO::getUsername),
                ANY_VALUE(userDef.avatarFileUrl).as(DepUserVO::getAvatarFileUrl),
                MAX(positionDef.level).as(DepUserDetailVO::getPosMaxLevel),
                GROUP_CONCAT(new FunctionWrapper.GroupConcat(positionDef.name).separator(",")).as(DepUserDetailVO::getPosNames)
        )
                .FROM(def)
                .INNER_JOIN(userDef).ON(userDef.id.eq(def.userId))
                .LEFT_JOIN(userPosDef).ON(userDef.id.eq(userPosDef.userId))
                .LEFT_JOIN(positionDef).ON(userPosDef.positionId.eq(positionDef.id))
                .GROUP_BY(userDef.id);
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(userDef.name.like(value));
                case "userIdNe" -> sqlWrapper.WHERE(def.userId.ne(value));
                case "depId" -> sqlWrapper.WHERE(def.depId.eq(value));
                case "depIds" -> sqlWrapper.WHERE(def.depId.in(values));
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
    protected Object doSort(OrderItem orderItem, DepUserDef def) {
        return switch (orderItem.getSortBy()) {
            case "name" -> UserDef.table().name;
            case "userName" -> UserDef.table().username;
            case "posLevel" -> LambdaUtils.getFieldName(DepUserDetailVO::getPosMaxLevel);
            default -> null;
        };
    }

    /**
     * 默认排序 当不满足doSort()时，会使用这里返回的内容进行排序
     *
     * @param def 数据库映射
     * @return 可以是多个排序条件的数组，按顺序进行排序
     */
    @Override
    protected List<DefaultOrderBy> defaultSort(DepUserDef def) {
        return new OrderBuilder().orderDesc(def.createTime).build();
    }
}
