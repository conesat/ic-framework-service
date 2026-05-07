package cn.icframework.system.config;

import cn.icframework.system.init.MenuInit;
import cn.icframework.system.init.PosInit;
import cn.icframework.system.init.RpInit;
import cn.icframework.system.init.helper.InitHelper;
import cn.icframework.system.runner.InitRunner;
import cn.icframework.system.task.ClearOnlineUser;
import cn.icframework.system.task.ClearSysFile;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(prefix = "ic.system", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnProperty(prefix = "ic.system", name = "jobs.enabled", havingValue = "true", matchIfMissing = true)
@Import({
        AsyncConfig.class,
        InitHelper.class,
        MenuInit.class,
        PosInit.class,
        RpInit.class,
        InitRunner.class,
        ClearOnlineUser.class,
        ClearSysFile.class
})
public class SystemJobAutoConfiguration {
}
