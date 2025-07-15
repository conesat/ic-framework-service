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
import cn.icframework.system.module.notice.def.NoticeDef;
import cn.icframework.system.module.notice.pojo.vo.NoticeMineVO;
import cn.icframework.system.module.noticereceiver.def.NoticeReceiverDef;
import cn.icframework.system.module.user.def.UserDef;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static cn.icframework.system.module.notice.wrapperbuilder.MineNoticeWrapperBuilder.QUERY_FIELDS.*;

/**
 * wrapper构建器
 *
 * @author ic
 * @date 2024/09/12
 */
@Component
public class MineNoticeWrapperBuilder extends BasicWrapperBuilder<NoticeDef> {
    public interface QUERY_FIELDS {
        String ENABLE = "enable";
        String USER_ID = "userId";
        String USER_ID_UNREAD = "userIdUnRead";
        String USER_ID_READ = "userIdRead";
        String CREATE_TIME = "createTime";
        String READ_TIME = "readTime";
    }

    /**
     * 返回一个数据库定义对象
     */
    public MineNoticeWrapperBuilder() {
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
        NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
        SqlWrapper sqlWrapper = SELECT(def._all, userDef.name.as(NoticeMineVO::getUserName), noticeReceiverDef.readTime.as(NoticeMineVO::getReadTime))
                .FROM(def)
                .LEFT_JOIN(userDef).ON(userDef.id.eq(def.userId))
                .INNER_JOIN(noticeReceiverDef).ON(noticeReceiverDef.noticeId.eq(def.id));
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case ParamsConst.SEARCH_KEY -> sqlWrapper.WHERE(def.title.like(value), OR(), userDef.name.like(value));
                case ENABLE -> sqlWrapper.WHERE(def.status.eq(Status.ENABLE.code()), def.enableTime.ge(LocalDateTime.now()).or().enableTime.isNull());
                case USER_ID -> {
                    // 用户所有通知
                    sqlWrapper.WHERE(noticeReceiverDef.userId.eq(value).hidden.ne(true).noticeId.eq(def.id));
                }
                case USER_ID_UNREAD -> {
                    // 用户未读通知
                    sqlWrapper.WHERE(noticeReceiverDef.userId.eq(value).hidden.ne(true).readTime.isNull().noticeId.eq(def.id));
                }
                case USER_ID_READ -> {
                    // 用户已通知
                    sqlWrapper.WHERE(noticeReceiverDef.userId.eq(value).hidden.ne(true).readTime.notNull().noticeId.eq(def.id));
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
    protected QueryField<?> doSort(OrderItem orderItem, NoticeDef def) {
        NoticeReceiverDef noticeReceiverDef = NoticeReceiverDef.table();
        return switch (orderItem.getSortBy()) {
            case CREATE_TIME -> def.createTime;
            case READ_TIME -> noticeReceiverDef.readTime;
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
