package cn.icframework.system.module.ws;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

public interface WebSocketSessionRegistry {

    void register(String userId, Long sessionId, WebSocketSession session, long expireAtMillis);

    void unregister(WebSocketSession session);

    Optional<WebSocketSession> getLocalSession(String userId);

    Optional<WebSocketRouteRecord> getRoute(String userId);

    String getNodeId();

    default boolean isCurrentNode(WebSocketRouteRecord routeRecord) {
        return routeRecord != null && getNodeId().equals(routeRecord.getNodeId());
    }

    boolean sendToLocalUser(String userId, TextMessage message);
}
