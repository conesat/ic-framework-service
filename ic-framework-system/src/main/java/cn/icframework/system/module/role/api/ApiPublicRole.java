package cn.icframework.system.module.role.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @since 2023/06/20
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/role", name = "角色")
public class ApiPublicRole extends BasicApi{

}
