package cn.icframework.hotel.module.scheduling.api;

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
@RequestMapping(value = Api.API_PUBLIC + "/scheduling", name ="排班")
public class ApiPublicScheduling extends BasicApi {

}
