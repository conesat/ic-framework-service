package cn.icframework.test;

import cn.icframework.test.model.User;
import cn.icframework.test.model.def.UserDef;
import cn.icframework.test.model.service.UserNormalService;
import cn.icframework.test.model.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hzl
 * @date 2025/7/11
 */
@SpringBootTest(classes = TestApplication.class)
public class TestMybatis {
    @Autowired
    private UserService userService;
    @Autowired
    private UserNormalService userNormalService;

    @Test
    public void testInsert() {
        userService.delete(UserDef.table());
        StringBuilder lName = new StringBuilder();
        lName.append("1".repeat(500));

        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 2000; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("user" + i);
            user.setPassword("password" + i);
            user.setNickname(i > 1000 ? lName.toString() : "nickname" + i);
            user.setAvatar("avatar" + i);
            userList.add(user);
        }
        userService.insertBatchTest(userList);
//        userNormalService.insertBatchTest(userList);
    }

    @Test
    public void testSelectIc() {
        long startTime = System.currentTimeMillis();
        UserDef userDef = UserDef.table();
        List<User> select = userService.select(userDef.id.ge(1000).id.le(5000));
        System.out.println(select.size());
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("ic time: " + executionTime + " ms");
    }

    @Test
    public void testSelectMybatis() {
        long startTime = System.currentTimeMillis();
        List<User> select = userNormalService.selectNormal();
        System.out.println(select.size());
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("ic time: " + executionTime + " ms");
    }


}