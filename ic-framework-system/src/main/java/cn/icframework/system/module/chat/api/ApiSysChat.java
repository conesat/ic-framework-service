package cn.icframework.system.module.chat.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chat.pojo.dto.ChatDTO;
import cn.icframework.system.module.chat.pojo.vo.ChatVO;
import cn.icframework.system.module.chat.pojo.vo.ChatVOConverter;
import cn.icframework.system.module.chat.service.ChatService;
import cn.icframework.system.module.chat.wrapperbuilder.ChatWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @since 2025/01/17
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/chat", name = "聊天")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysChat extends BasicApi {
    private final ChatService chatService;
    private final ChatVOConverter chatVOConverter;
    private final ChatWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<ChatVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(chatService.selectById(id, ChatVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex [int] 当前页码
     * @param pageSize  [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ChatVO> page(HttpServletRequest request, PageRequest page) {
        QueryParams queryParams = getQueryParams(request);
        if (!JWTUtils.isSu()) {
            // 普通用户只能查询自己的
            queryParams.put(ChatWrapperBuilder.BuildColumn.userId, JWTUtils.getUserId());
        }
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return chatService.page(sqlWrapper, page, ChatVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        chatService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated ChatDTO dto) {
        chatService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@Validated ChatDTO dto) {
        chatService.edit(dto);
        return Response.success();
    }
}
