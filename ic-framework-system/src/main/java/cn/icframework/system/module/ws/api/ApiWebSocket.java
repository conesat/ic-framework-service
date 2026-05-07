package cn.icframework.system.module.ws.api;

import cn.icframework.auth.standard.IOnlineUserService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.cache.utils.CacheUtils;
import cn.icframework.system.module.chatmsg.ChatMsg;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgDTO;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgSendDTO;
import cn.icframework.system.module.chatmsg.service.ChatMsgService;
import cn.icframework.system.module.ws.WebSocketSessionRegistry;
import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class ApiWebSocket extends TextWebSocketHandler {
    private static final String TOKEN_CACHE_SESSION_ID_PREFIX = "IC:TOKEN:SESSION_ID:";

    private final ObjectProvider<ChatMsgService> chatMsgServiceProvider;
    private final WebSocketSessionRegistry webSocketSessionRegistry;
    private final IOnlineUserService onlineUserService;

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        try {
            MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(Objects.requireNonNull(session.getUri())).build().getQueryParams();
            String token = queryParams.getFirst("token");
            DecodedJWT decodedJWT = JWTUtils.verifyToken(token);
            if (decodedJWT == null) {
                session.close(CloseStatus.BAD_DATA);
                return;
            }
            String userId = decodedJWT.getSubject();
            Long sessionId = JWTUtils.getTokenSessionId(decodedJWT);
            if (!StringUtils.hasLength(userId) || sessionId == null) {
                session.close(CloseStatus.BAD_DATA);
                return;
            }
            onlineUserService.verify(userId, sessionId);
            long ttlSeconds = CacheUtils.getExpire(TOKEN_CACHE_SESSION_ID_PREFIX + sessionId);
            long expireAt = System.currentTimeMillis() + Math.max(ttlSeconds, 1L) * 1000L;
            webSocketSessionRegistry.register(userId, sessionId, session, expireAt);
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
        String payload = message.getPayload();
        ChatMsgSendDTO wsMessage = JSONObject.parseObject(payload, ChatMsgSendDTO.class);
        ChatMsgService msgService = chatMsgServiceProvider.getIfAvailable();
        if (msgService == null) {
            log.warn("ChatMsgService未装配，忽略WebSocket消息持久化");
            return;
        }
        if (StringUtils.hasLength(wsMessage.getToUserId())) {
            ChatMsg msg = new ChatMsg();
            BeanUtils.copyExcludeProps(wsMessage, msg, ChatMsgDTO::getId);
            msgService.insert(msg);
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        webSocketSessionRegistry.unregister(session);
    }
}
