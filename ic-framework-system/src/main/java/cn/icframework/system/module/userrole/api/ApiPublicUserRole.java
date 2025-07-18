package cn.icframework.system.module.userrole.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public接口会有缓存，并且不校验用户信息
 * 用于公开内容
 *
 * @author ic generator
 * @since 2023/08/09
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/user-role", name ="用户权限")
public class ApiPublicUserRole extends BasicApi {

}
