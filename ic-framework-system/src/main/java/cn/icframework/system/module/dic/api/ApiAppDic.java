package cn.icframework.system.module.dic.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.system.consts.UserType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @since 2023/06/14
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/dic", name = "字典")
public class ApiAppDic extends BasicApi{
}
