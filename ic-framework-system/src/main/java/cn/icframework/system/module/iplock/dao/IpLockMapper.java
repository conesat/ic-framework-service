package cn.icframework.system.module.iplock.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.system.module.iplock.IpLock;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ic
* @since 2025/07/22
*/
@Mapper
public interface IpLockMapper extends BasicMapper<IpLock> {
}
