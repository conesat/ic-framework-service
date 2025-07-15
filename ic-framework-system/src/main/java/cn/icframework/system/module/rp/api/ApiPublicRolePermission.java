package cn.icframework.system.module.rp.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ic generator
 * @date 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/role-permission", name = "角色权限")
public class ApiPublicRolePermission extends BasicApi {

}
