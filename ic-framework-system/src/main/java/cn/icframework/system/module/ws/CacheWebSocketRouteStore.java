package cn.icframework.system.module.ws;

import cn.icframework.cache.utils.CacheUtils;
import com.alibaba.fastjson2.JSON;

import java.util.Optional;

public class CacheWebSocketRouteStore implements WebSocketRouteStore {
    private static final String ROUTE_CACHE_PREFIX = "IC:WS:ROUTE:";

    @Override
    public void save(String userId, WebSocketRouteRecord routeRecord, long ttlSeconds) {
        CacheUtils.set(routeCacheKey(userId), JSON.toJSONString(routeRecord), ttlSeconds);
    }

    @Override
    public Optional<WebSocketRouteRecord> get(String userId) {
        Object routeJson = CacheUtils.get(routeCacheKey(userId));
        if (!(routeJson instanceof String routeValue) || routeValue.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(JSON.parseObject(routeValue, WebSocketRouteRecord.class));
    }

    @Override
    public void remove(String userId) {
        CacheUtils.remove(routeCacheKey(userId));
    }

    private String routeCacheKey(String userId) {
        return ROUTE_CACHE_PREFIX + userId;
    }
}
