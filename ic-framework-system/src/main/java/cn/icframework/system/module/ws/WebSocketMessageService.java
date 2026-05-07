package cn.icframework.system.module.ws;

import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgSendDTO;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketMessageService {
    private final ObjectProvider<WebSocketSessionRegistry> webSocketSessionRegistryProvider;

    public boolean send(ChatMsgSendDTO msgDTO) {
        WebSocketSessionRegistry webSocketSessionRegistry = webSocketSessionRegistryProvider.getIfAvailable();
        if (webSocketSessionRegistry == null) {
            log.debug("WebSocket未启用，跳过消息实时投递, toUserId={}", msgDTO.getToUserId());
            return false;
        }
        WebSocketRouteRecord routeRecord = webSocketSessionRegistry.getRoute(msgDTO.getToUserId()).orElse(null);
        if (routeRecord == null) {
            return false;
        }
        if (!webSocketSessionRegistry.isCurrentNode(routeRecord)) {
            log.info("用户{}在线，但连接位于节点{}，当前节点{}不执行跨节点投递", msgDTO.getToUserId(), routeRecord.getNodeId(), webSocketSessionRegistry.getNodeId());
            return false;
        }
        return webSocketSessionRegistry.sendToLocalUser(msgDTO.getToUserId(), new TextMessage(JSON.toJSONString(msgDTO)));
    }
}
