package cn.icframework.hotel.module.timescope.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @date 2024/11/26
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/time-scope", name ="时间表")
public class ApiPublicTimeScope extends BasicApi {

}
