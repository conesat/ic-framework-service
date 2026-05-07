package cn.icframework.system.module.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class DefaultWebSocketSessionRegistry implements WebSocketSessionRegistry {
    private final ConcurrentMap<String, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> sessionBusinessIdMap = new ConcurrentHashMap<>();
    private final String nodeId;
    private final WebSocketRouteStore routeStore;

    public DefaultWebSocketSessionRegistry(WebSocketRouteStore routeStore, String appName) {
        this.routeStore = routeStore;
        this.nodeId = appName + ":" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public void register(String userId, Long sessionId, WebSocketSession session, long expireAtMillis) {
        WebSocketSession oldSession = userSessionMap.put(userId, session);
        Long oldBusinessSessionId = oldSession == null ? null : sessionBusinessIdMap.get(oldSession.getId());
        sessionUserMap.put(session.getId(), userId);
        sessionBusinessIdMap.put(session.getId(), sessionId);

        if (oldSession != null && oldSession != session && oldSession.isOpen()) {
            try {
                oldSession.close();
            } catch (IOException e) {
                log.warn("关闭旧的WebSocket连接失败, userId={}", userId, e);
            }
        }

        WebSocketRouteRecord routeRecord = new WebSocketRouteRecord(userId, sessionId, nodeId, expireAtMillis);
        long ttlSeconds = Math.max(1L, (expireAtMillis - System.currentTimeMillis()) / 1000L);
        routeStore.save(userId, routeRecord, ttlSeconds);

        if (oldSession != null && oldBusinessSessionId != null && !oldBusinessSessionId.equals(sessionId)) {
            log.debug("用户{}的WebSocket业务会话从{}切换到{}", userId, oldBusinessSessionId, sessionId);
        }
    }

    @Override
    public void unregister(WebSocketSession session) {
        String userId = sessionUserMap.remove(session.getId());
        Long businessSessionId = sessionBusinessIdMap.remove(session.getId());
        if (userId == null) {
            return;
        }
        userSessionMap.computeIfPresent(userId, (key, currentSession) -> currentSession == session ? null : currentSession);
        Optional<WebSocketRouteRecord> routeRecord = getRoute(userId);
        WebSocketRouteRecord currentRoute = routeRecord.orElse(null);
        if (currentRoute != null
                && nodeId.equals(currentRoute.getNodeId())
                && businessSessionId != null
                && businessSessionId.equals(currentRoute.getSessionId())) {
            routeStore.remove(userId);
        }
    }

    @Override
    public Optional<WebSocketSession> getLocalSession(String userId) {
        WebSocketSession session = userSessionMap.get(userId);
        if (session == null || !session.isOpen()) {
            if (session != null) {
                userSessionMap.remove(userId, session);
            }
            return Optional.empty();
        }
        return Optional.of(session);
    }

    @Override
    public Optional<WebSocketRouteRecord> getRoute(String userId) {
        return routeStore.get(userId);
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public boolean sendToLocalUser(String userId, TextMessage message) {
        Optional<WebSocketSession> sessionOptional = getLocalSession(userId);
        if (sessionOptional.isEmpty()) {
            return false;
        }
        try {
            sessionOptional.get().sendMessage(message);
            return true;
        } catch (IOException e) {
            unregister(sessionOptional.get());
            log.warn("发送WebSocket消息失败, userId={}", userId, e);
            return false;
        }
    }
}
