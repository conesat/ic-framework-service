package cn.icframework.system.module.chat.wrapperbuilder;

import cn.icframework.core.basic.wrapperbuilder.BasicWrapperBuilder;
import cn.icframework.core.basic.wrapperbuilder.DefaultOrderBy;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.OrderItem;
import cn.icframework.core.common.consts.ParamsConst;
import cn.icframework.mybatis.query.QueryField;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.chat.def.ChatDef;
import cn.icframework.system.module.chat.pojo.vo.ChatWithUserVO;
import cn.icframework.system.module.chatmsg.def.ChatMsgDef;
import cn.icframework.system.module.chatuser.def.ChatUserDef;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.system.module.user.def.UserDef;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static cn.icframework.mybatis.wrapper.FunctionWrapper.COUNT;
import static cn.icframework.mybatis.wrapper.FunctionWrapper.EXISTS;

/**
 * wrapper构建器
 *
 * @author ic
 * @since 2025/01/17
 */
@Component
public class ChatWrapperBuilder extends BasicWrapperBuilder<ChatDef> {

    /**
     * 定义可用的入参字段
     * 根据这些参数构建wrapper 和 排序
     */
    public interface BuildColumn {
        String id = "id";
        String userId = "userId";
    }

    /**
     * 返回一个数据库定义对象
     */
    public ChatWrapperBuilder() {
        super(ChatDef.table());
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
    protected SqlWrapper list(QueryParams params, ChatDef def) {
        SqlWrapper sqlWrapper = SELECT(def._all).FROM(def);
        params.forEach((key, rv) -> {
            Object value = rv.getValue(); // 单个参数
            Object[] values = rv.getValues(); // 数组参数
            switch (key) {
                case BuildColumn.id -> sqlWrapper.WHERE(def.id.eq(value));
                case BuildColumn.userId -> {
                    ChatUserDef chatUserDef = ChatUserDef.table();
                    sqlWrapper.WHERE(EXISTS(SELECT().FROM(chatUserDef).WHERE(chatUserDef.chatId.eq(def.id).userId.eq(value))));
                }
                case ParamsConst.SEARCH_KEY -> {
                    ChatUserDef chatUserDef = ChatUserDef.table();
                    UserDef userDef = UserDef.table();
                    sqlWrapper.WHERE(
                            def.name.like(value),
                            OR(),
                            EXISTS(SELECT()
                                    .FROM(chatUserDef)
                                    .INNER_JOIN(userDef).ON(chatUserDef.userId.eq(userDef.id))
                                    .WHERE(chatUserDef.chatId.eq(def.id))
                                    .AND(userDef.name.like(value))
                            )
                    );
                }
            }
        });
        return sqlWrapper;
    }

    /**
     * 获取单个聊天信息
     *
     * @param subject 当前用户
     * @param userId  聊天对象
     * @return
     */
    public SqlWrapper chat(String subject, String userId) {
        ChatDef chatDef = ChatDef.table();
        ChatUserDef chatUserDef_0 = ChatUserDef.table();
        ChatUserDef chatUserDef_1 = ChatUserDef.table();
        return SELECT()
                .FROM(chatDef)
                .WHERE(
                        EXISTS(SELECT(1)
                                .FROM(chatUserDef_0)
                                .WHERE(chatUserDef_0.chatId.eq(chatDef.id).userId.eq(subject)))
                )
                .AND(
                        EXISTS(SELECT(1)
                                .FROM(chatUserDef_1)
                                .WHERE(chatUserDef_1.chatId.eq(chatDef.id).userId.eq(userId)))
                );
    }

    /**
     * 查询列表
     *
     * @param subject
     * @param params
     * @param page
     * @return
     */
    public SqlWrapper chatList(String subject, QueryParams params, PageRequest page) {
        SqlWrapper sqlWrapper = build(params);
        // 先对主体内容分页，然后联合查询用户名和头像
        ChatDef chatDef = sqlWrapper.PAGE(page).AS(ChatDef.class);
        SqlWrapper unreadCountSqlWrapper = getUnreadCountSqlWrapper(subject);
        return SELECT(chatDef._all,
                unreadCountSqlWrapper.AS(ChatWithUserVO::getUnreadCount)
        )
                .FROM(chatDef)
                .ORDER_BY_DESC(chatDef.lastMsgTime);
    }

    /**
     * 获取聊天前4个用户信息
     */
    public SqlWrapper top4(Long id, String subject) {
        // 每一个聊天获取前4个用户信息
        ChatUserDef chatUserDef = ChatUserDef.table();
        UserDef userDef = UserDef.table();
        return SELECT(userDef._all)
                .FROM(userDef)
                .INNER_JOIN(chatUserDef).ON(chatUserDef.userId.eq(userDef.id))
                .WHERE(chatUserDef.chatId.eq(id).userId.ne(subject))
                .LIMIT(4);
    }

    /**
     * 获取未读消息数 子查询
     *
     * @param subject 当前用户
     * @return
     */
    private static SqlWrapper getUnreadCountSqlWrapper(String subject) {
        ChatUserDef chatUserDef = ChatUserDef.table();
        ChatMsgDef chatMsgDef = ChatMsgDef.table();
        return SELECT(COUNT("1"))
                .FROM(chatUserDef)
                .LEFT_JOIN(chatMsgDef).ON(chatMsgDef.chatId.eq(chatUserDef.chatId))
                .WHERE(chatUserDef.userId.eq(subject)
                        .userId.ne(chatMsgDef.userId))
                .AND(chatUserDef.readTime.le(chatMsgDef.createTime).or().readTime.isNull());
    }


    ////// 排序处理分割线 ---------------------------------------------------------

    /**
     * 处理排序条件，页面传参时需要将排序条件转成json字符串，key必须是 orders。例如 orders:"[{column:'排序字段或自定义内容', asc:false},{column:'排序字段或自定义内容', asc:true}]"
     *
     * @param orderItem orders已经在BasicParamsHandler转成了 OrderItem数组。这里就是遍历每一项排序内容
     * @param def       数据库映射
     * @return 这里只需要返回排序的数据库字段 或者 其他条件内容。倒序还是正序是根据OrderItem判断的
     */
    @Override
    protected QueryField<?> doSort(OrderItem orderItem, ChatDef def) {
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
    protected List<DefaultOrderBy> defaultSort(ChatDef def) {
        // 使用以下方法构建默认排序
        // return new OrderBuilder().orderDesc(def.createTime).build();
        return Collections.emptyList();
    }
}
