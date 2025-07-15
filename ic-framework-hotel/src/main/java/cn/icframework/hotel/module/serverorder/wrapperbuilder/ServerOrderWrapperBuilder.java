package cn.icframework.hotel.module.serverorder.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import cn.icframework.hotel.module.room.def.RoomDef;
import cn.icframework.hotel.module.roomconsumeorder.def.RoomConsumeOrderDef;
import cn.icframework.hotel.module.roomorder.def.RoomOrderDef;
import cn.icframework.hotel.module.serverorder.def.ServerOrderDef;
import cn.icframework.hotel.module.serverorder.pojo.vo.ServerOrderVO;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.user.def.UserDef;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static cn.icframework.mybatis.wrapper.FunctionWrapper.EXISTS;

/**
 * wrapper构建器
 *
 * @author ic
 * @date 2024/11/29
 */
@Component
public class ServerOrderWrapperBuilder extends BasicWrapperBuilder<ServerOrderDef> {

    /**
     * 定义可用的入参字段
     * 根据这些参数构建wrapper 和 排序
     */
    public interface BuildColumn {
        String id = "id";
        String target = "target";
        String targetId = "targetId";
        String doTimeLe = "doTimeLe";
        String doTimeGe = "doTimeGe";
        String floorId = "floorId";
        String roomId = "roomId";
        String acceptTimeIsNull = "acceptTimeIsNull";
        String serverUserId = "serverUserId";
    }

    /**
     * 返回一个数据库定义对象
     */
    public ServerOrderWrapperBuilder() {
        super(ServerOrderDef.table());
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
    protected SqlWrapper list(QueryParams params, ServerOrderDef def) {
        UserDef userDef = UserDef.table();
        SqlWrapper sqlWrapper = SELECT(def._all,
                userDef.name.as(ServerOrderVO::getServerUserName),
                userDef.avatarFileUrl.as(ServerOrderVO::getServerUserAvatarUrl))
                .FROM(def)
                .LEFT_JOIN(userDef).ON(userDef.id.eq(def.serverUserId));
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case BuildColumn.id -> sqlWrapper.WHERE(def.id.eq(value));
                // case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.name.like(value));
                case BuildColumn.target -> sqlWrapper.WHERE(def.target.eq(value));
                case BuildColumn.targetId -> sqlWrapper.WHERE(def.targetId.eq(value));
                case BuildColumn.doTimeLe -> sqlWrapper.WHERE(def.doTime.le(value));
                case BuildColumn.doTimeGe -> sqlWrapper.WHERE(def.doTime.ge(value));
                case BuildColumn.acceptTimeIsNull -> sqlWrapper.WHERE(def.acceptTime.isNull());
                case BuildColumn.serverUserId -> sqlWrapper.WHERE(def.serverUserId.eq(value));
                case BuildColumn.floorId -> {
                    RoomDef roomDef = RoomDef.table();
                    // 找出房间清洁订单
                    SqlWrapper clearWrapper = SELECT(1)
                            .FROM(roomDef)
                            .WHERE(def.target.eq(ServerTargetEnum.CLEAR.code()), roomDef.id.eq(def.targetId).floorId.eq(value));

                    // 找出房间消费订单
                    RoomConsumeOrderDef roomConsumeOrderDef = RoomConsumeOrderDef.table();
                    RoomOrderDef roomOrderDef = RoomOrderDef.table();
                    SqlWrapper saleWrapper = SELECT(1)
                            .FROM(roomConsumeOrderDef)
                            .INNER_JOIN(roomOrderDef).ON(roomOrderDef.id.eq(roomConsumeOrderDef.roomOrderId))
                            .INNER_JOIN(roomDef).ON(roomOrderDef.roomId.eq(roomDef.id))
                            .WHERE(def.target.eq(ServerTargetEnum.SALE.code()), roomConsumeOrderDef.id.eq(def.targetId), roomDef.floorId.eq(value));
                    sqlWrapper.WHERE(EXISTS(clearWrapper), OR(), EXISTS(saleWrapper));
                }
                case BuildColumn.roomId -> {
                    // 找出房间消费订单
                    RoomConsumeOrderDef roomConsumeOrderDef = RoomConsumeOrderDef.table();
                    RoomOrderDef roomOrderDef = RoomOrderDef.table();
                    SqlWrapper safeWrapper = SELECT(1)
                            .FROM(roomConsumeOrderDef)
                            .INNER_JOIN(roomOrderDef).ON(roomOrderDef.id.eq(roomConsumeOrderDef.roomOrderId))
                            .WHERE(def.target.eq(ServerTargetEnum.SALE.code()), roomConsumeOrderDef.id.eq(def.targetId), roomOrderDef.roomId.eq(value));
                    sqlWrapper.WHERE(def.target.eq(ServerTargetEnum.CLEAR.code()).targetId.eq(value), OR(), EXISTS(safeWrapper));
                }
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
    protected QueryField<?> doSort(OrderItem orderItem, ServerOrderDef def) {
        return switch (orderItem.getSortBy()) {
            // case BuildColumn.id -> def.id;
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
    protected List<DefaultOrderBy> defaultSort(ServerOrderDef def) {
        // 使用以下方法构建默认排序
        // return new OrderBuilder().orderDesc(def.createTime).build();
        return Collections.emptyList();
    }
}
