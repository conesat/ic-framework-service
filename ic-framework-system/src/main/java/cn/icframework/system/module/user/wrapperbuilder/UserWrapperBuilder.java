package cn.icframework.system.module.user.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.depuser.def.DepUserDef;
import cn.icframework.system.module.noticereceiver.def.NoticeReceiverDef;
import cn.icframework.system.module.position.def.PositionDef;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.userpos.def.UserPosDef;
import cn.icframework.system.module.userrole.def.UserRoleDef;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.icframework.mybatis.wrapper.FunctionWrapper.EXISTS;
import static cn.icframework.mybatis.wrapper.FunctionWrapper.NOT_EXISTS;


/**
 * @author iceFire
 * @since 2023/6/15
 */
@Component
public class UserWrapperBuilder extends BasicWrapperBuilder<UserDef> {

    public UserWrapperBuilder() {
        super(UserDef.table());
    }


    /**
     * 定义可用的入参字段
     * 根据这些参数构建wrapper 和 排序
     */
    public interface BuildColumn {
        String id = "id";
        String su = "su";
        String notInDepId = "notInDepId";
        String notInPosId = "notInPosId";
        String notInNoticeId = "notInNoticeId";
        String depIds = "depIds";
        String roleIds = "roleIds";
        String posIds = "posIds";
        String posSigns = "posSigns";
        String noUserId = "noUserId";
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
    protected SqlWrapper list(QueryParams params, UserDef def) {
        SqlWrapper sqlWrapper = SELECT()
                .FROM(def);
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.name.like(value).or().username.like(value));
                case BuildColumn.id -> sqlWrapper.WHERE(def.id.eq(value));
                case BuildColumn.su -> sqlWrapper.WHERE(def.su.eq(value));
                case BuildColumn.noUserId -> sqlWrapper.WHERE(def.id.ne(value));
                case BuildColumn.notInDepId -> {
                    DepUserDef depUserDef = DepUserDef.table();
                    sqlWrapper.WHERE(NOT_EXISTS(SELECT(1).FROM(depUserDef).WHERE(depUserDef.depId.eq(value).userId.eq(def.id))));
                }
                case BuildColumn.notInPosId -> {
                    UserPosDef userPosDef = UserPosDef.table();
                    sqlWrapper.WHERE(NOT_EXISTS(SELECT(1).FROM(userPosDef).WHERE(userPosDef.positionId.eq(value).userId.eq(def.id))));
                }
                case BuildColumn.notInNoticeId -> {
                    NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
                    sqlWrapper.WHERE(NOT_EXISTS(SELECT(1).FROM(noticeReceiverDef).WHERE(noticeReceiverDef.noticeId.eq(value).userId.eq(def.id))));
                }
                case BuildColumn.depIds -> {
                    DepUserDef depUserDef = DepUserDef.table();
                    sqlWrapper.WHERE(EXISTS(SELECT(1).FROM(depUserDef).WHERE(depUserDef.depId.in(values).userId.eq(def.id))));
                }
                case BuildColumn.roleIds -> {
                    UserRoleDef userRoleDef = UserRoleDef.table();
                    sqlWrapper.WHERE(EXISTS(SELECT(1).FROM(userRoleDef).WHERE(userRoleDef.roleId.in(values).userId.eq(def.id))));
                }
                case BuildColumn.posIds -> {
                    UserPosDef userPosDef = UserPosDef.table();
                    sqlWrapper.WHERE(EXISTS(SELECT(1).FROM(userPosDef).WHERE(userPosDef.positionId.in(values).userId.eq(def.id))));
                }
                case BuildColumn.posSigns -> {
                    UserPosDef userPosDef = UserPosDef.table();
                    PositionDef positionDef = PositionDef.table();
                    sqlWrapper.WHERE(EXISTS(SELECT(1).FROM(userPosDef)
                            .LEFT_JOIN(positionDef).ON(positionDef.id.eq(userPosDef.positionId))
                            .WHERE(positionDef.sign.in(values), userPosDef.userId.eq(def.id))));
                }
            }
        });
        return sqlWrapper;
    }

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicWrapperBuilder转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, UserDef def) {
        return switch (orderItem.getSortBy()) {
            case "createTime" -> def.createTime;
            case "name" -> def.name;
            case "username" -> def.username;
            case "updateTime" -> def.updateTime;
            case "status" -> def.status;
            default -> null;
        };
    }

    /**
     * 默认排序 当不满足doSort时，会使用这里返回的内容进行排序
     *
     * @return 可以是多个排序条件的数组，按顺序进行排序
     */
    @Override
    protected List<DefaultOrderBy> defaultSort(UserDef def) {
        return new OrderBuilder().orderDesc(def.createTime).build();
    }

}
