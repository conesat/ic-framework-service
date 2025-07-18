package cn.icframework.system.module.onlineuser.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.onlineuser.pojo.vo.OnlineUserVO;
import cn.icframework.system.module.onlineuser.service.OnlineUserService;
import cn.icframework.system.module.onlineuser.wrapperbuilder.OnlineUserWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @since 2024/09/11
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/online-user", name ="在线用户")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysOnlineUser extends BasicApi {
    private final OnlineUserService onlineUserService;
    private final OnlineUserWrapperBuilder wrapperBuilder;

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<OnlineUserVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return onlineUserService.page(sqlWrapper, page, OnlineUserVO.class);
    }

    /**
     * 强制退出
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "强制退出")
    public Response<Void> delete(@RequestParam("ids") List<Long> ids) {
        onlineUserService.logout(ids);
        return Response.success();
    }
}
