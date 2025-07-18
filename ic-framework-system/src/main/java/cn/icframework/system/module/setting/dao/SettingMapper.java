package cn.icframework.system.module.setting.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.system.module.setting.Setting;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hzl
 * @since 2023/6/5
 */
@Mapper
public interface SettingMapper extends BasicMapper<Setting> {
}
