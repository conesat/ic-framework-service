package cn.icframework.system.module.dep.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @date 2023/06/21
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/dept", name = "部门")
public class ApiPublicDept extends BasicApi{

}
