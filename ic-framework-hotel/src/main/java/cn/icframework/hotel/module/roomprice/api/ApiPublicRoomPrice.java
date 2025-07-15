package cn.icframework.hotel.module.roomprice.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @date 2024/10/14
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/room-price", name ="房价")
public class ApiPublicRoomPrice extends BasicApi {

}
