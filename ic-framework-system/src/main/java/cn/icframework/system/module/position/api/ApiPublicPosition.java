package cn.icframework.system.module.position.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public接口会有缓存，并且不校验用户信息
 * 用于公开内容
 *
 * @author ic
 * @date 2024/09/09
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/position", name ="职位")
public class ApiPublicPosition extends BasicApi {

}
