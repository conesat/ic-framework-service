package cn.icframework.project.module.energy.swapstation.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.project.module.energy.swapstation.SwapStation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app 接口
 *
 * @author ic
 * @since 2026/07/13
 */
@RequireAuth
@RestController
@RequestMapping(value = Api.API_APP + "/swap-station", name ="换电站")
public class ApiAppSwapStation extends BasicApi {

}
