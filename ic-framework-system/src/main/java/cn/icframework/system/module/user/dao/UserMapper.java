package cn.icframework.system.module.user.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.system.module.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hzl
 * @date 2023/5/31
 */
@Mapper
public interface UserMapper extends BasicMapper<User> {
}
