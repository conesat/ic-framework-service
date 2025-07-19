package cn.icframework.system.module.user.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.Response;
import cn.icframework.system.common.RegisterLoginHelper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.onlineuser.service.OnlineUserService;
import cn.icframework.system.module.user.pojo.vo.UserLoginInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


/**
 * @author hzl
 * @since 2023/5/31
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/user", name = "用户")
@RequiredArgsConstructor
public class ApiPublicUser extends BasicApi {
    private final RegisterLoginHelper registerLoginHelper;
    private final OnlineUserService onlineUserService;

    /**
     * 管理员登录
     *
     * @param username 用户名
     * @param passwd   密码
     * @param code     验证码
     * @return 登录信息
     */
    @PostMapping("/login")
    public UserLoginInfo login(HttpServletRequest request,
                               @RequestParam("username") String username,
                               @RequestParam("passwd") String passwd,
                               String code) {
        return onlineUserService.login(request, username, passwd, code, null);
    }

    /**
     * app登录
     *
     * @param username 用户名
     * @param passwd   密码
     * @param code     验证码
     * @return 登录信息
     */
    @PostMapping("/app-login")
    public UserLoginInfo appLogin(HttpServletRequest request,
                                  @RequestParam("username") String username,
                                  @RequestParam("passwd") String passwd,
                                  String code) {
        return onlineUserService.login(request, username, passwd, code, (int) Duration.ofDays(7).toSeconds());
    }

    /**
     * 获取加密密钥
     *
     * @param username 用户名
     * @return 密钥
     */
    @GetMapping("/key")
    public Response<String> key(@RequestParam("username") String username) {
        return Response.success(registerLoginHelper.buildKey(UserType.SYSTEM_USER, username));
    }

    /**
     * 获取图片验证码
     *
     * @param username 用户名
     * @return 图片base 64
     */
    @GetMapping("/captcha")
    public Response<String> captcha(@RequestParam("username") String username) {
        return Response.success(registerLoginHelper.buildCaptcha(UserType.SYSTEM_USER, username));
    }
}
