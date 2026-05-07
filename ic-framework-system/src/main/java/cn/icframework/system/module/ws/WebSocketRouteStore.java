package cn.icframework.system.module.ws;

import java.util.Optional;

public interface WebSocketRouteStore {

    void save(String userId, WebSocketRouteRecord routeRecord, long ttlSeconds);

    Optional<WebSocketRouteRecord> get(String userId);

    void remove(String userId);
}
