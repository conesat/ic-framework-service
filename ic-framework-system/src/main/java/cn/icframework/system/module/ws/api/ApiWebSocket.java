package cn.icframework.system.module.ws.api;

import cn.icframework.core.utils.BeanUtils;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.system.module.chatmsg.ChatMsg;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgDTO;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgSendDTO;
import cn.icframework.system.module.chatmsg.service.ChatMsgService;
import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiWebSocket extends TextWebSocketHandler {
    private final static Map<String, WebSocketSession> USER_ID_SESSION_MAP = new HashMap<>();
    private final static Map<String, String> SESSION_ID_USER_ID_MAP = new HashMap<>();

    private final ChatMsgService msgService;

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        // 连接建立后的逻辑
        try {
            String query = Objects.requireNonNull(session.getUri()).getQuery();
            if (query != null) {
                Map<String, String> queryParams = Arrays.stream(query.split("&"))
                        .map(param -> param.split("="))
                        .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
                String token = queryParams.get("token");
                DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
                String userId = decodedJWT.getSubject();
                if (userId == null || userId.isEmpty()) {
                    session.close(CloseStatus.BAD_DATA);
                    return;
                }
                USER_ID_SESSION_MAP.put(userId, session);
                SESSION_ID_USER_ID_MAP.put(session.getId(), userId);
            } else {
                session.close(CloseStatus.BAD_DATA);
            }
        } catch (Exception e) {
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException ioException) {
                log.error("ws登录异常", ioException);
            }
        }
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) {
        // 处理接收到的消息
        String payload = message.getPayload();
        ChatMsgSendDTO wsMessage = JSONObject.parseObject(payload, ChatMsgSendDTO.class);
        if (StringUtils.hasLength(wsMessage.getToUserId())) {
            ChatMsg msg = ChatMsg.def();
            BeanUtils.copyExcludeProps(wsMessage, msg, ChatMsgDTO::getId);
            msgService.insert(msg);
        }
    }

    /**
     * 发送消息
     *
     * @param msgDTO
     */
    public static void send(ChatMsgSendDTO msgDTO) {
        WebSocketSession webSocketSession = USER_ID_SESSION_MAP.get(msgDTO.getToUserId());
        if (webSocketSession != null) {
            try {
                webSocketSession.sendMessage(new TextMessage(JSONObject.toJSONString(msgDTO)));
            } catch (IOException ignore) {
            }
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        try {
            // 连接关闭后的逻辑
            String userId = SESSION_ID_USER_ID_MAP.get(session.getId());
            SESSION_ID_USER_ID_MAP.remove(session.getId());
            USER_ID_SESSION_MAP.remove(userId);
        } catch (Exception ignore) {
        }
    }
}