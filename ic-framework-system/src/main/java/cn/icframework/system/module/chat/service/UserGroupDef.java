package cn.icframework.system.module.chat.service;

import cn.icframework.mybatis.query.QueryField;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.def.UserDef;

/**
 * 用于查询用户组的中间表
 */
public class UserGroupDef extends UserDef {
    public UserGroupDef(Class<?> defClass) {
        super(defClass);
    }

    public static UserGroupDef table() {
        return new UserGroupDef(User.class);
    }

    @Override
    public UserGroupDef newInstance() {
        return new UserGroupDef(UserGroupDef.class);
    }

    public QueryField<UserGroupDef> chatId = new QueryField<>(this, "chat_id");
}