package cn.icframework.system.module.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketRouteRecord {
    private String userId;
    private Long sessionId;
    private String nodeId;
    private long expireAt;
}
