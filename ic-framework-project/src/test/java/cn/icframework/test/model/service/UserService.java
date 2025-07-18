package cn.icframework.test.model.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.test.model.User;
import cn.icframework.test.model.dao.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ic
 * @since 2024/09/25
 */
@Service
public class UserService extends BasicService<UserMapper, User> {

    @Transactional
    public void insertBatchTest(List<User> userList) {
        long startTime = System.currentTimeMillis();
        // 批量插入用户
        insertBatch(userList, true);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Test execution time1: " + executionTime + " ms");
    }
}
