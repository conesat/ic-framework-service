package cn.icframework.system.module.sysfile.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @date 2023/06/21
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/sys-file", name = "文件")
public class ApiPublicSysFile extends BasicApi {

}
