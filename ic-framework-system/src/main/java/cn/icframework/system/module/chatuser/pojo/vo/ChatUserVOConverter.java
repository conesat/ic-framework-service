package cn.icframework.system.module.chatuser.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chatuser.ChatUser;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.user.service.IUserInfoProvider;
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

    private final IUserInfoProvider userInfoProvider;

    public ChatUserVOConverter(@Autowired UserService userService, @Autowired(required = false) IUserInfoProvider userInfoProvider) {
        this.userService = userService;
        this.userInfoProvider = userInfoProvider;
    }

    @Override
    protected void enhance(List<ChatUserVO> list) {
        Map<String, ChatUserVO> voMap = list.stream().collect(Collectors.toMap(ChatUserVO::getUserId, f -> f));
        Map<String, List<String>> userTypeIdMap = list.stream().collect(Collectors.groupingBy(ChatUserVO::getUserType,
                Collectors.mapping(ChatUserVO::getUserId, Collectors.toList())));
        for (String key : userTypeIdMap.keySet()) {
            if(Objects.equals(key, UserType.SYSTEM_USER)) {
                List<User> userList = userService.select(UserDef.table().id.in(userTypeIdMap.get(key)));
                for (User user : userList) {
                    ChatUserVO chatUserVO = voMap.get(user.getId().toString());
                    if (chatUserVO != null) {
                        chatUserVO.setUserName(user.getName());
                        chatUserVO.setUserPic(user.getAvatarFileUrl());
                    }
                }
            } else {
                List<IUserInfoProvider.BaseUserInfo> userList = userInfoProvider.getInfo(key, userTypeIdMap.get(key));
                for (IUserInfoProvider.BaseUserInfo user : userList) {
                    ChatUserVO chatUserVO = voMap.get(user.getUserId());
                    if (chatUserVO != null) {
                        chatUserVO.setUserName(user.getUserName());
                        chatUserVO.setUserPic(user.getUserPic());
                    }
                }
            }
        }
    }
}
