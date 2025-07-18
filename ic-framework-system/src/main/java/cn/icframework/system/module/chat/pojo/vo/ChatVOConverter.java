package cn.icframework.system.module.chat.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.system.module.chat.Chat;
import cn.icframework.system.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ic
 * @since 2025/01/17
 */
@Component
@RequiredArgsConstructor
public class ChatVOConverter extends BasicConverter<Chat, ChatVO> {
    final private UserService userService;

    public List<ChatVO> handlerChatName(List<ChatVO> chatVOS) {
        List<Long> chatIdList = chatVOS.stream().map(ChatVO::getId).toList();
        return chatVOS;
    }
}
