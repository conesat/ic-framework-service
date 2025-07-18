package cn.icframework.test.model.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.test.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ic
* @since 2024/09/25
*/
@Mapper
public interface UserMapper extends BasicMapper<User> {
}
