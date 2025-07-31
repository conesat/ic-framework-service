package cn.icframework.system.module.updatehistory.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.system.consts.UserType;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app 接口
 *
 * @author ic
 * @since 2025/07/31
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/update-history", name ="更新历史")
public class ApiAppUpdateHistory extends BasicApi {

}
