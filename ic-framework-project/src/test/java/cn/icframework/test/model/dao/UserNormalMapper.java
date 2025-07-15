package cn.icframework.test.model.dao;

import cn.icframework.test.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author ic
* @date 2024/09/25
*/
@Mapper
public interface UserNormalMapper  {
    void insertBatchNormal(@Param("userList") List<User> userList);

    List<User> selectNormal();
}
