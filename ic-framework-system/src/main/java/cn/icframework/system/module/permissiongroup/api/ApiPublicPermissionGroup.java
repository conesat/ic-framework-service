package cn.icframework.system.module.permissiongroup.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public接口会有缓存，并且不校验用户信息
 * 用于公开内容
 *
 * @author ic
 * @since 2024/08/02
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/permission-group", name ="权限分组")
public class ApiPublicPermissionGroup extends BasicApi {

}
