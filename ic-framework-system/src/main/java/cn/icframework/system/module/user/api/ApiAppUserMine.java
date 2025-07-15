package cn.icframework.system.module.user.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.common.consts.TokenInfo;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.onlineuser.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @date 2023/06/17
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/mine/user",name = "用户")
@RequiredArgsConstructor
public class ApiAppUserMine extends BasicApi  {
    private final OnlineUserService onlineUserService;

    /**
     * 刷新token
     * 无需鉴权
     *
     * @return
     */
    @RequireAuth(onlyToken = true)
    @PostMapping("/refresh-token")
    public Response<TokenInfo> refreshToken() {
        return Response.success(onlineUserService.refreshToken());
    }


    /**
     * 退出登录
     */
    @PostMapping(value = "logout", name = "退出登录")
    public Response<Void> logout() {
        onlineUserService.logout(JWTUtils.getTokenSessionId());
        return Response.success();
    }
}
