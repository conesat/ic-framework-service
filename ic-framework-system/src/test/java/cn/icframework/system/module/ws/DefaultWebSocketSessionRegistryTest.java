package cn.icframework.system.module.ws;

import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultWebSocketSessionRegistryTest {

    @Test
    void unregisterOldSessionShouldNotDeleteLatestRoute() {
        InMemoryRouteStore routeStore = new InMemoryRouteStore();
        DefaultWebSocketSessionRegistry registry = new DefaultWebSocketSessionRegistry(routeStore, "test-app");
        TestWebSocketSession oldSession = new TestWebSocketSession("ws-old");
        TestWebSocketSession newSession = new TestWebSocketSession("ws-new");

        registry.register("u1", 101L, oldSession, Instant.now().plusSeconds(30).toEpochMilli());
        registry.register("u1", 102L, newSession, Instant.now().plusSeconds(30).toEpochMilli());

        registry.unregister(oldSession);

        WebSocketRouteRecord routeRecord = registry.getRoute("u1").orElse(null);
        assertNotNull(routeRecord);
        assertEquals(102L, routeRecord.getSessionId());
        assertTrue(registry.getLocalSession("u1").isPresent());
        assertEquals("ws-new", registry.getLocalSession("u1").get().getId());
    }

    static class InMemoryRouteStore implements WebSocketRouteStore {
        private final java.util.Map<String, WebSocketRouteRecord> store = new java.util.HashMap<>();

        @Override
        public void save(String userId, WebSocketRouteRecord routeRecord, long ttlSeconds) {
            store.put(userId, routeRecord);
        }

        @Override
        public java.util.Optional<WebSocketRouteRecord> get(String userId) {
            return java.util.Optional.ofNullable(store.get(userId));
        }

        @Override
        public void remove(String userId) {
            store.remove(userId);
        }
    }

    static class TestWebSocketSession implements WebSocketSession {
        private final String id;
        private boolean open = true;

        TestWebSocketSession(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public URI getUri() {
            return URI.create("ws://localhost/ws");
        }

        @Override
        public HttpHeaders getHandshakeHeaders() {
            return new HttpHeaders();
        }

        @Override
        public Map<String, Object> getAttributes() {
            return Collections.emptyMap();
        }

        @Override
        public Principal getPrincipal() {
            return null;
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return null;
        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public String getAcceptedProtocol() {
            return null;
        }

        @Override
        public void setTextMessageSizeLimit(int messageSizeLimit) {
        }

        @Override
        public int getTextMessageSizeLimit() {
            return 0;
        }

        @Override
        public void setBinaryMessageSizeLimit(int messageSizeLimit) {
        }

        @Override
        public int getBinaryMessageSizeLimit() {
            return 0;
        }

        @Override
        public List<WebSocketExtension> getExtensions() {
            return Collections.emptyList();
        }

        @Override
        public void sendMessage(WebSocketMessage<?> message) throws IOException {
        }

        @Override
        public boolean isOpen() {
            return open;
        }

        @Override
        public void close() throws IOException {
            this.open = false;
        }

        @Override
        public void close(CloseStatus status) throws IOException {
            this.open = false;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
