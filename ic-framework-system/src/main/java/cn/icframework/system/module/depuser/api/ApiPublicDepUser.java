package cn.icframework.system.module.depuser.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public接口会有缓存，并且不校验用户信息
 * 用于公开内容
 *
 * @author ic
 * @since 2024/08/11
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/dep-user", name ="部门用户")
public class ApiPublicDepUser extends BasicApi {

}
