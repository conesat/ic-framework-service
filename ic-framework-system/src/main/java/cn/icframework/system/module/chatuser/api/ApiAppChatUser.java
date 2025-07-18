package cn.icframework.system.module.chatuser.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chatuser.service.ChatUserService;
import cn.icframework.system.module.chatuser.wrapperbuilder.ChatUserWrapperBuilder;
import cn.icframework.system.module.user.pojo.vo.UserSimpleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app 接口
 *
 * @author ic
 * @since 2025/01/17
 */
@RequireAuth(userType = UserType.SYSTEM_USER)
@RestController
@RequiredArgsConstructor
@RequestMapping(value = Api.API_APP + "/chat-user", name = "聊天用户关联")
public class ApiAppChatUser extends BasicApi {
    private final ChatUserWrapperBuilder wrapperBuilder;
    private final ChatUserService chatUserService;

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all-user", name = "查询全部")
    public List<UserSimpleVO> allUser(@RequestParam String chatId, String searchKey) {
        return chatUserService.allUser(chatId, searchKey);
    }
}
