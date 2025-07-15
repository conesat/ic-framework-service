package cn.icframework.hotel.module.hotel.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.hotel.module.hotel.Hotel;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ic
* @date 2024/09/24
*/
@Mapper
public interface HotelMapper extends BasicMapper<Hotel> {
}
