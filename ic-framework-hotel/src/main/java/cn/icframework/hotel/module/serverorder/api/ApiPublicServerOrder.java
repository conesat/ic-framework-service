package cn.icframework.hotel.module.serverorder.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.common.enums.ServerStateEnum;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @date 2024/11/29
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/server-order", name = "服务订单")
public class ApiPublicServerOrder extends BasicApi {

    /**
     * 服务状态枚举
     */
    @Cacheable(cacheNames = "state-enums")
    @GetMapping(value = "/state-enums", name = "服务状态枚举")
    public Response<ServerStateEnum[]> stateEnums() {
        return Response.success(ServerStateEnum.values());
    }

    /**
     * 服务类型枚举
     */
    @Cacheable(cacheNames = "target-enums")
    @GetMapping(value = "/target-enums", name = "服务类型枚举")
    public Response<ServerTargetEnum[]> targetEnums() {
        return Response.success(ServerTargetEnum.values());
    }
}
