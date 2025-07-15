package cn.icframework.system.module.permission.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @date 2023/06/20
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/permission",name = "权限")
public class ApiPublicPermission extends BasicApi {

}
