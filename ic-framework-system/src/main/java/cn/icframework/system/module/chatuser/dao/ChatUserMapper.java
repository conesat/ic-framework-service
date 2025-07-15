package cn.icframework.system.module.chatuser.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.system.module.chatuser.ChatUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ic
* @date 2025/01/17
*/
@Mapper
public interface ChatUserMapper extends BasicMapper<ChatUser> {
}
