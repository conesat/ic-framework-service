package cn.icframework.system.module.notice.wrapperbuilder;

import cn.icframework.common.enums.Status;
import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.OrderBuilder;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.depuser.def.DepUserDef;
import cn.icframework.system.module.notice.def.NoticeDef;
import cn.icframework.system.module.notice.pojo.vo.NoticeVO;
import cn.icframework.system.module.noticereceiver.NoticeReceiver;
import cn.icframework.system.module.noticereceiver.def.NoticeReceiverDef;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.userpos.def.UserPosDef;
import cn.icframework.system.module.userrole.def.UserRoleDef;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static cn.icframework.mybatis.wrapper.FunctionWrapper.NOT_EXISTS;
import static cn.icframework.system.module.notice.wrapperbuilder.NoticeWrapperBuilder.QUERY_FIELDS.ENABLE;

/**
 * wrapper构建器
 *
 * @author ic
 * @date 2024/09/12
 */
@Component
public class NoticeWrapperBuilder extends BasicWrapperBuilder<NoticeDef> {

    public interface QUERY_FIELDS {
        String ENABLE = "enable";
    }

    /**
     * 返回一个数据库定义对象
     */
    public NoticeWrapperBuilder() {
        super(NoticeDef.table());
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
    protected SqlWrapper list(QueryParams params, NoticeDef def) {
        UserDef userDef = UserDef.table();
        SqlWrapper sqlWrapper = SELECT(def._all, userDef.name.as(NoticeVO::getUserName))
                .FROM(def)
                .LEFT_JOIN(userDef).ON(userDef.id.eq(def.userId));
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.title.like(value), OR(), userDef.name.like(value));
                case ENABLE ->
                        sqlWrapper.WHERE(def.status.eq(Status.ENABLE.code()), def.enableTime.ge(LocalDateTime.now()).or().enableTime.isNull());
            }
        });
        return sqlWrapper;
    }

    /**
     * 分发通知到部门
     *
     * @param noticeId
     * @param depIds
     * @return
     */
    public SqlWrapper distributeDep(Long noticeId, Long[] depIds) {
        NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
        DepUserDef depUserDef = DepUserDef.table();
        SqlWrapper select = SELECT(AS(noticeId, NoticeReceiver::getNoticeId), depUserDef.id.as(NoticeReceiver::getUserId))
                .FROM(depUserDef)
                .WHERE(depUserDef.id.in(depIds))
                .AND(NOT_EXISTS(SELECT(1)
                        .FROM(noticeReceiverDef).
                        WHERE(noticeReceiverDef.noticeId.eq(noticeId), noticeReceiverDef.userId.eq(depUserDef.id))));
        return INSERT().INTO(NoticeReceiver.class)
                .COLUMNS(NoticeReceiver::getNoticeId, NoticeReceiver::getUserId)
                .VALUES(select);
    }

    /**
     * 分发通知到角色
     *
     * @param noticeId
     * @param depIds
     * @return
     */
    public SqlWrapper distributeRole(Long noticeId, Long[] roleIds) {
        NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
        UserRoleDef userRoleDef = UserRoleDef.table();
        SqlWrapper select = SELECT(AS(noticeId, NoticeReceiver::getNoticeId), userRoleDef.id.as(NoticeReceiver::getUserId))
                .FROM(userRoleDef)
                .WHERE(userRoleDef.id.in(roleIds))
                .AND(NOT_EXISTS(SELECT(1)
                        .FROM(noticeReceiverDef).
                        WHERE(noticeReceiverDef.noticeId.eq(noticeId), noticeReceiverDef.userId.eq(userRoleDef.id))));
        return INSERT().INTO(NoticeReceiver.class)
                .COLUMNS(NoticeReceiver::getNoticeId, NoticeReceiver::getUserId)
                .VALUES(select);
    }

    /**
     * 分发通知到岗位
     *
     * @param noticeId
     * @param posIds
     * @return
     */
    public SqlWrapper distributePos(Long noticeId, Long[] posIds) {
        NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
        UserPosDef userPosDef = UserPosDef.table();
        SqlWrapper select = SELECT(AS(noticeId, NoticeReceiver::getNoticeId), userPosDef.id.as(NoticeReceiver::getUserId))
                .FROM(userPosDef)
                .WHERE(userPosDef.id.in(posIds))
                .AND(NOT_EXISTS(SELECT(1)
                        .FROM(noticeReceiverDef).
                        WHERE(noticeReceiverDef.noticeId.eq(noticeId), noticeReceiverDef.userId.eq(userPosDef.id))));
        return INSERT().INTO(NoticeReceiver.class)
                .COLUMNS(NoticeReceiver::getNoticeId, NoticeReceiver::getUserId)
                .VALUES(select);
    }

    /**
     * 分发通知到用户
     *
     * @param noticeId
     * @param userIds
     * @return
     */
    public SqlWrapper distributeUser(Long noticeId, Long[] userIds) {
        NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
        UserDef userDef = UserDef.table();
        SqlWrapper select = SELECT(AS(noticeId, NoticeReceiver::getNoticeId), userDef.id.as(NoticeReceiver::getUserId))
                .FROM(userDef)
                .WHERE(userDef.id.in(userIds))
                .AND(
                        NOT_EXISTS(
                            SELECT(1) .FROM(noticeReceiverDef).WHERE(noticeReceiverDef.noticeId.eq(noticeId), noticeReceiverDef.userId.eq(userDef.id))
                        )
                );
        return INSERT().INTO(NoticeReceiver.class)
                .COLUMNS(NoticeReceiver::getNoticeId, NoticeReceiver::getUserId)
                .VALUES(select);
    }

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicParamsHandler转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @param def       数据库映射
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, NoticeDef def) {
        return switch (orderItem.getSortBy()) {
            case "createTime" -> def.createTime;
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
    protected List<DefaultOrderBy> defaultSort(NoticeDef def) {
        // 使用以下方法构建默认排序
        return new OrderBuilder().orderDesc(def.createTime).build();
    }
}
