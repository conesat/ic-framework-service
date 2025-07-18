package cn.icframework.test;

import cn.icframework.test.model.service.UserNormalService;
import cn.icframework.test.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author hzl
 * @since 2025/7/11
 */
@SpringBootTest(classes = TestApplication.class)
public class TestMybatis {
    @Autowired
    private UserService userService;
    @Autowired
    private UserNormalService userNormalService;

}