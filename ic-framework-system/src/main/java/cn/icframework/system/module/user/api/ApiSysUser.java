package cn.icframework.system.module.user.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.core.utils.Assert;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.core.utils.MD5Util;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.pojo.dto.UserDTO;
import cn.icframework.system.module.user.pojo.vo.UserDetailVO;
import cn.icframework.system.module.user.pojo.vo.UserVOConverter;
import cn.icframework.system.module.user.service.UserService;
import cn.icframework.system.module.user.wrapperbuilder.UserWrapperBuilder;
import cn.icframework.system.utils.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
 * @author ic generator
 * @date 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/user", name = "用户")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysUser extends BasicApi {
    private final UserService userService;
    private final UserVOConverter userVOConverter;
    private final UserWrapperBuilder wrapperBuilder;

    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<UserDetailVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(userService.selectById(id, UserDetailVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<UserDetailVO> page(HttpServletRequest request, PageRequest page) {
        QueryParams queryParams = getQueryParams(request);
        if (!JWTUtils.isSu()) {
            queryParams.put(UserWrapperBuilder.BuildColumn.su, false);
        }
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return userService.page(sqlWrapper, page, UserDetailVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        userService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑或者保存
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated UserDTO dto) {
        userService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated UserDTO dto) {
        userService.edit(dto);
        return Response.success();
    }

    /**
     * 重置密码
     */
    @PutMapping(value = "/update-passwd", name = "重置密码")
    public Response<Void> updatePassWord(@RequestParam("userId") String userId) {
        User user = userService.selectById(userId);
        Assert.isNotNull(user, "用户不存在");
        user.setPasswd(MD5Util.encode(user.getUsername() + MD5Util.encode(user.getUsername() + PasswordUtils.generateRandomPassword(8))));
        userService.updateById(user);
        return Response.success();
    }
}
