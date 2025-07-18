package cn.icframework.system.module.onlineuser.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2024/09/11
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/online-user", name ="在线用户")
public class ApiPublicOnlineUser extends BasicApi {

}
