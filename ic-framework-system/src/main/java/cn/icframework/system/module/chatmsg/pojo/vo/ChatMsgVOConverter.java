package cn.icframework.system.module.chatmsg.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.system.module.chatmsg.ChatMsg;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ic
 * @since 2025/01/17
 */
@Component
public class ChatMsgVOConverter extends BasicConverter<ChatMsg, ChatMsgVO> {

    @Override
    protected void enhance(List<ChatMsgVO> list) {
        handlerBaseUserInfo(list);
    }

    public void handlerBaseUserInfo(List<ChatMsgVO> list) {

    }
}
