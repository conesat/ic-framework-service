package cn.icframework.system.module.iplock.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2025/07/22
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/ip-lock", name ="ip登录锁")
public class ApiPublicIpLock extends BasicApi {

}
