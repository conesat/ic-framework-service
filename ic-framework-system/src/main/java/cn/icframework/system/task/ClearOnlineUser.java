package cn.icframework.system.task;

import cn.icframework.system.module.onlineuser.def.OnlineUserDef;
import cn.icframework.system.module.onlineuser.service.OnlineUserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static cn.icframework.mybatis.wrapper.Wrapper.SELECT;

@Component
@RequiredArgsConstructor
public class ClearOnlineUser {
    private final OnlineUserService onlineUserService;


    /**
     * 清理过期用户
     * 1分钟一次
     * TODO 分布式需要调整，用xxlJob，或者其他分布式任务替换
     */
    @PostConstruct
    @Scheduled(fixedRate = 60000)
    public void clear() {
        OnlineUserDef onlineUserDef = OnlineUserDef.table();
        List<Long> ids = onlineUserService.select(SELECT(onlineUserDef.sessionId).FROM(onlineUserDef).WHERE(onlineUserDef.expireTime.lt(LocalDateTime.now())), Long.class);
        onlineUserService.logout(ids);
        List<Long> onlineIds = onlineUserService.select(SELECT(onlineUserDef.sessionId).FROM(onlineUserDef).WHERE(onlineUserDef.expireTime.ge(LocalDateTime.now())), Long.class);
        for (Long onlineId : onlineIds) {
            onlineUserService.recoveryUserOnlineStatus(onlineId);
        }
    }
}
