package cn.icframework.system.module.chat.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chat.Chat;
import cn.icframework.system.module.chat.pojo.vo.ChatVO;
import cn.icframework.system.module.chat.pojo.vo.ChatVOConverter;
import cn.icframework.system.module.chat.pojo.vo.ChatWithUserVO;
import cn.icframework.system.module.chat.service.ChatService;
import cn.icframework.system.module.chat.wrapperbuilder.ChatWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app 接口
 *
 * @author ic
 * @since 2025/01/17
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/chat", name = "聊天")
@RequiredArgsConstructor
public class ApiAppChat extends BasicApi {
    private final ChatService chatService;
    private final ChatVOConverter chatVOConverter;
    private final ChatWrapperBuilder wrapperBuilder;

    /**
     * 创建一对一聊天
     *
     * @param dto
     */
    @PostMapping(path = "create-sys-single/{userId}", name = "创建聊天")
    public Response<ChatVO> createSysSingle(@PathVariable("userId") String userId) {
        Chat chat = chatService.createSysSingle(JWTUtils.getSubject(), userId);
        return Response.success(chatVOConverter.convert(chat));
    }


    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ChatWithUserVO> page(HttpServletRequest request, PageRequest page) {
        QueryParams queryParams = getQueryParams(request);
        queryParams.put(ChatWrapperBuilder.BuildColumn.userId, JWTUtils.getSubject());
        return chatService.pageWithUser(JWTUtils.getSubject(), queryParams, page);
    }
}
