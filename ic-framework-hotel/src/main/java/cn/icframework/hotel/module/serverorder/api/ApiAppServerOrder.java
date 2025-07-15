package cn.icframework.hotel.module.serverorder.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.hotel.module.serverorder.pojo.vo.ServerOrderVO;
import cn.icframework.hotel.module.serverorder.service.ServerOrderService;
import cn.icframework.hotel.module.serverorder.wrapperbuilder.ServerOrderWrapperBuilder;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app 接口
 *
 * @author ic
 * @date 2024/11/29
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/server-order", name ="服务订单")
@RequiredArgsConstructor
public class ApiAppServerOrder extends BasicApi {
    private final ServerOrderWrapperBuilder wrapperBuilder;
    private final ServerOrderService serverOrderService;

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ServerOrderVO> page(HttpServletRequest request, PageRequest page) {
        QueryParams queryParams = getQueryParams(request);
        queryParams.put(ServerOrderWrapperBuilder.BuildColumn.serverUserId, JWTUtils.getSubject());
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return serverOrderService.page(sqlWrapper, page, ServerOrderVO.class);
    }
}
