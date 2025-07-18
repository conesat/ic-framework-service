package cn.icframework.system.module.onlineuser.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.onlineuser.pojo.vo.OnlineUserVO;
import cn.icframework.system.module.onlineuser.service.OnlineUserService;
import cn.icframework.system.module.onlineuser.wrapperbuilder.OnlineUserWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @since 2024/09/11
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/online-user", name = "在线用户")
@RequireAuth(userType = UserType.SYSTEM_USER, onlyToken = true)
@RequiredArgsConstructor
public class ApiSysMineOnlineUser extends BasicApi {
    private final OnlineUserService onlineUserService;
    private final OnlineUserWrapperBuilder wrapperBuilder;

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page-mine", name = "分页查询")
    public PageResponse<OnlineUserVO> pageMine(HttpServletRequest request, PageRequest page) {
        QueryParams queryParams = getQueryParams(request);
        queryParams.put("userId", JWTUtils.getUserId());
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return onlineUserService.page(sqlWrapper, page, OnlineUserVO.class);
    }

    /**
     * 强制退出
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(value = "/mine", name = "强制退出")
    public Response<Void> deleteMine(@RequestParam("ids") List<Long> ids) {
        onlineUserService.logout(ids, JWTUtils.getUserId());
        return Response.success();
    }
}
