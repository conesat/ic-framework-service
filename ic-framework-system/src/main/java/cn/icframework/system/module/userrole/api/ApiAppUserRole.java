package cn.icframework.system.module.userrole.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.system.consts.UserType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * protect 接口需要校验用户信息和权限
 * 用于登录用户相关内容
 *
 * @author ic generator
 * @since 2023/08/09
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/user-role", name ="用户权限")
public class ApiAppUserRole extends BasicApi {

}
