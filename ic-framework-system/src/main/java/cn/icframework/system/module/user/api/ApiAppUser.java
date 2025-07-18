package cn.icframework.system.module.user.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.user.pojo.vo.UserDetailVO;
import cn.icframework.system.module.user.service.UserService;
import cn.icframework.system.module.user.wrapperbuilder.UserWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author create by ic gen
 * @since 2023/06/17
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/user",name = "用户")
@RequiredArgsConstructor
public class ApiAppUser extends BasicApi  {
    private final UserService userService;
    private final UserWrapperBuilder wrapperBuilder;

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
        queryParams.put(UserWrapperBuilder.BuildColumn.noUserId, JWTUtils.getSubject());
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return userService.page(sqlWrapper, page, UserDetailVO.class);
    }

}
