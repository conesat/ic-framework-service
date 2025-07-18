package cn.icframework.test.model.service;

import cn.icframework.test.model.User;
import cn.icframework.test.model.dao.UserNormalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ic
 * @since 2024/09/25
 */
@Service
public class UserNormalService {
    @Autowired
    private UserNormalMapper userMapper;

    public void insertBatchNormal(List<User> userList) {
        userMapper.insertBatchNormal(userList);
    }

    public List<User> selectNormal() {
        return userMapper.selectNormal();
    }

    @Transactional
    public void insertBatchTest(List<User> userList) {
        long startTime = System.currentTimeMillis();
        int batchSize = 1000; // 每批插入1000条
        for (int i = 0; i < userList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, userList.size());
            List<User> batchList = userList.subList(i, end);
            insertBatchNormal(batchList);
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Test execution time2: " + executionTime + " ms");
    }
}
