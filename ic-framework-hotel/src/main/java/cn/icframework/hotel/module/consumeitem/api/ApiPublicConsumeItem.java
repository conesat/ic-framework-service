package cn.icframework.hotel.module.consumeitem.api;

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
@RequestMapping(value = Api.API_PUBLIC + "/consume-item", name ="消费项目")
public class ApiPublicConsumeItem extends BasicApi {

}
