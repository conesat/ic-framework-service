package cn.icframework.system.task;

import cn.icframework.system.module.sysfile.def.SysFileDef;
import cn.icframework.system.module.sysfile.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static cn.icframework.mybatis.wrapper.Wrapper.AND;
import static cn.icframework.mybatis.wrapper.Wrapper.DELETE;

@Component
@RequiredArgsConstructor
public class ClearSysFile {
    private final SysFileService sysFileService;

    /**
     * 清理无用文件
     * 1天一次
     * TODO 分布式需要调整，用xxlJob，或者其他分布式任务替换
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void clear() {
        SysFileDef sysFileDef = SysFileDef.table();
        // 删除无引用，且创建时间超过一天的
        sysFileService.delete(DELETE().FROM(sysFileDef).WHERE(sysFileDef.createTime.lt(LocalDateTime.now().minusDays(1)), AND(sysFileDef.refCount.isNull().or().refCount.eq(0))));
    }
}
