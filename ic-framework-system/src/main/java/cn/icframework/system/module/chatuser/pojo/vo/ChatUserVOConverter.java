package cn.icframework.system.module.chatuser.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chatuser.ChatUser;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ic
 * @since 2025/01/17
 */
@Component
public class ChatUserVOConverter extends BasicConverter<ChatUser, ChatUserVO> {
    private final UserService userService;


    public ChatUserVOConverter(@Autowired UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void enhance(List<ChatUserVO> list) {
        Map<String, ChatUserVO> voMap = list.stream().collect(Collectors.toMap(ChatUserVO::getUserId, f -> f));
        List<User> userList = userService.select(UserDef.table().id.in(voMap.keySet().stream().toList()));
        for (User user : userList) {
            ChatUserVO chatUserVO = voMap.get(user.getId().toString());
            if (chatUserVO != null) {
                chatUserVO.setUserName(user.getName());
                chatUserVO.setUserPic(user.getAvatarFileUrl());
            }
        }
    }
}
