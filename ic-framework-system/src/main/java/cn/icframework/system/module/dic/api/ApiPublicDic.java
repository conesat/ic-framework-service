package cn.icframework.system.module.dic.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @date 2023/06/14
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/dic", name = "字典")
public class ApiPublicDic extends BasicApi{

}
