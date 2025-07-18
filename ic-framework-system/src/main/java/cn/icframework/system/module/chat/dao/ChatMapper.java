package cn.icframework.system.module.chat.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.system.module.chat.Chat;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ic
 * @since 2025/01/17
 */
@Mapper
public interface ChatMapper extends BasicMapper<Chat> {
}
