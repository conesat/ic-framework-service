package cn.icframework.hotel.module.hotel.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @date 2024/09/24
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/hotel", name ="酒店")
public class ApiPublicHotel extends BasicApi {

}
