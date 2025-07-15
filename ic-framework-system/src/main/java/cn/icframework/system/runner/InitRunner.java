package cn.icframework.system.runner;

import cn.icframework.system.init.MenuInit;
import cn.icframework.system.init.PosInit;
import cn.icframework.system.init.RpInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * 程序启动完成 需要初始化权限
 *
 * @author hzl
 * @date 2022/6/21 10:37
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class InitRunner implements CommandLineRunner {
    private final RpInit rpInit;
    private final MenuInit menuInit;
    private final PosInit posInit;


    @Override
    public void run(String... args) throws Exception {
        rpInit.initRp();
        menuInit.initMenu();
        posInit.initPos();
    }


}
