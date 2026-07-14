package cn.icframework.project.module.energy.swaporder.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.project.module.energy.swaporder.SwapOrder;
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
@RequestMapping(value = Api.API_PUBLIC + "/swap-order", name ="换电订单")
public class ApiPublicSwapOrder extends BasicApi {

}
