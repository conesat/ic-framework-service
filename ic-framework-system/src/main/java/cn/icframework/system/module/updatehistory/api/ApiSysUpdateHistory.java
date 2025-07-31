package cn.icframework.system.module.updatehistory.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.updatehistory.pojo.vo.UpdateHistoryVO;
import cn.icframework.system.module.updatehistory.pojo.vo.UpdateHistoryVOConverter;
import cn.icframework.system.module.updatehistory.service.UpdateHistoryService;
import cn.icframework.system.module.updatehistory.wrapperbuilder.UpdateHistoryWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @since 2025/07/31
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/update-history", name ="更新历史")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysUpdateHistory extends BasicApi {
    private final UpdateHistoryService updateHistoryService;
    private final UpdateHistoryVOConverter updateHistoryVOConverter;
    private final UpdateHistoryWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<UpdateHistoryVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(updateHistoryService.selectById(id, UpdateHistoryVO.class));
    }

    /**
     * 获取列表
     *
     * @param current  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<UpdateHistoryVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return updateHistoryService.page(sqlWrapper, page, UpdateHistoryVO.class);
    }
}
