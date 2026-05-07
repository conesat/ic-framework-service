package cn.icframework.system.config;

import cn.icframework.system.module.ws.CacheWebSocketRouteStore;
import cn.icframework.system.module.ws.DefaultWebSocketSessionRegistry;
import cn.icframework.system.module.ws.WebSocketRouteStore;
import cn.icframework.system.module.ws.WebSocketSessionRegistry;
import cn.icframework.system.module.ws.api.ApiWebSocket;
import cn.icframework.auth.standard.IOnlineUserService;
import cn.icframework.system.module.chatmsg.service.ChatMsgService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "ic.system", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnProperty(prefix = "ic.system", name = "websocket.enabled", havingValue = "true", matchIfMissing = true)
public class SystemWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(WebSocketRouteStore.class)
    WebSocketRouteStore webSocketRouteStore() {
        return new CacheWebSocketRouteStore();
    }

    @Bean
    @ConditionalOnMissingBean(WebSocketSessionRegistry.class)
    WebSocketSessionRegistry webSocketSessionRegistry(WebSocketRouteStore webSocketRouteStore,
                                                      @Value("${spring.application.name:ic-framework}") String appName) {
        return new DefaultWebSocketSessionRegistry(webSocketRouteStore, appName);
    }

    @Bean
    @ConditionalOnMissingBean(ApiWebSocket.class)
    ApiWebSocket apiWebSocket(ObjectProvider<ChatMsgService> chatMsgServiceProvider,
                              WebSocketSessionRegistry webSocketSessionRegistry,
                              IOnlineUserService onlineUserService) {
        return new ApiWebSocket(chatMsgServiceProvider, webSocketSessionRegistry, onlineUserService);
    }

    @Bean
    @ConditionalOnMissingBean(WebSocketConfig.class)
    WebSocketConfig webSocketConfig(ApiWebSocket apiWebSocket) {
        return new WebSocketConfig(apiWebSocket);
    }
}
