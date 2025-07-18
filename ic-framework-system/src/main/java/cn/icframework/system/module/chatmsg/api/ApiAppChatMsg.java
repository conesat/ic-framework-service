package cn.icframework.system.module.chatmsg.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgDTO;
import cn.icframework.system.module.chatmsg.pojo.vo.ChatMsgVO;
import cn.icframework.system.module.chatmsg.service.ChatMsgService;
import cn.icframework.system.module.chatmsg.wrapperbuilder.ChatMsgWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * app 接口
 *
 * @author ic
 * @since 2025/01/17
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequestMapping(value = Api.API_APP + "/chat-msg", name = "消息")
@RequiredArgsConstructor
public class ApiAppChatMsg extends BasicApi {

    private final ChatMsgService msgService;
    private final ChatMsgWrapperBuilder wrapperBuilder;

    /**
     * 发送消息
     */
    @PostMapping(name = "发送消息")
    public Response<Void> create(@Validated ChatMsgDTO dto) {
        dto.setUserType(UserType.SYSTEM_USER);
        dto.setUserId(JWTUtils.getSubject());
        msgService.edit(dto);
        return Response.success();
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ChatMsgVO> page(HttpServletRequest request,
                                        PageRequest page,
                                        @RequestParam String chatId) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return msgService.page(sqlWrapper, page, ChatMsgVO.class);
    }

}
