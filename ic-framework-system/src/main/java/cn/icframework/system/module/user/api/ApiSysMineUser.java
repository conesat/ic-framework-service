package cn.icframework.system.module.user.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.common.consts.TokenInfo;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.menu.pojo.vo.MenuWithChildrenVO;
import cn.icframework.system.module.onlineuser.service.OnlineUserService;
import cn.icframework.system.module.user.pojo.dto.UserMineDTO;
import cn.icframework.system.module.user.pojo.vo.UserDetailVO;
import cn.icframework.system.module.user.service.UserService;
import cn.icframework.system.module.user.wrapperbuilder.UserWrapperBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author ic generator
 * @since 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/mine/user", name = "用户")
@RequireAuth(userType = UserType.SYSTEM_USER, onlyToken = true)
@RequiredArgsConstructor
public class ApiSysMineUser extends BasicApi {
    private final UserService userService;
    private final UserWrapperBuilder wrapperBuilder;
    private final OnlineUserService onlineUserService;

    /**
     * 获取个人详情
     */
    @GetMapping(value = "/mine", name = "获取个人详情")
    public Response<UserDetailVO> mine() {
        QueryParams queryParams = new QueryParams();
        queryParams.put("id", JWTUtils.getUserId());
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return Response.success(userService.selectOne(sqlWrapper, UserDetailVO.class));
    }

    /**
     * 编辑个人详情
     */
    @PutMapping(value = "/mine", name = "编辑个人详情")
    public Response<Void> editMine(@Validated UserMineDTO dto) {
        userService.editMine(JWTUtils.getUserId(), dto);
        return Response.success();
    }

    /**
     * 编辑个人详情
     */
    @PutMapping(value = "/update-passwd", name = "编辑个人详情")
    public Response<Void> updatePasswd(@RequestParam("passwd") String passwd, @RequestParam("passwdOld") String passwdOld) {
        userService.updatePasswd(JWTUtils.getUserId(), passwd, passwdOld);
        return Response.success();
    }

    /**
     * 编辑头像
     */
    @PutMapping(value = "edit-avatar", name = "编辑头像")
    public Response<Void> editAvatar(@RequestParam("avatarFileId") Long avatarFileId) {
        userService.editAvatar(JWTUtils.getUserId(), avatarFileId);
        return Response.success();
    }

    /**
     * 退出登录
     */
    @PostMapping(value = "logout", name = "退出登录")
    public Response<Void> logout() {
        onlineUserService.logout(JWTUtils.getTokenSessionId());
        return Response.success();
    }

    /**
     * 刷新token
     * 无需鉴权
     */
    @RequireAuth(onlyToken = true)
    @PostMapping("/refresh-token")
    public Response<TokenInfo> refreshToken() {
        return Response.success(onlineUserService.refreshToken());
    }

    /**
     * 获取用户菜单
     * 无需鉴权
     */
    @RequireAuth(onlyToken = true)
    @GetMapping("/menus")
    public Response<List<MenuWithChildrenVO>> menus() {
        return Response.success(userService.getMenu(JWTUtils.getUserId()));
    }

}
