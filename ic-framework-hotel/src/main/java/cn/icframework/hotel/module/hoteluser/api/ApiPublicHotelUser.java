package cn.icframework.hotel.module.hoteluser.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @date 2024/10/25
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/hotel-user", name ="酒店用户")
public class ApiPublicHotelUser extends BasicApi {

}
