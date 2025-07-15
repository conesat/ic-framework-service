package cn.icframework.hotel.module.hoteluser.dao;

import cn.icframework.mybatis.mapper.BasicMapper;
import cn.icframework.hotel.module.hoteluser.HotelUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ic
* @date 2024/10/25
*/
@Mapper
public interface HotelUserMapper extends BasicMapper<HotelUser> {
}
