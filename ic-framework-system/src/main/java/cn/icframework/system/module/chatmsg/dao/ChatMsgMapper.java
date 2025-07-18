package cn.icframework.system.module.chatmsg.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.system.module.chatmsg.ChatMsg;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ic
* @since 2025/01/17
*/
@Mapper
public interface ChatMsgMapper extends BasicMapper<ChatMsg> {
}
