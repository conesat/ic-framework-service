package cn.icframework.project.module.energy.swapbattery.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.project.module.energy.swapbattery.SwapBattery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2026/07/13
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/swap-battery", name ="换电电池")
public class ApiPublicSwapBattery extends BasicApi {

}
