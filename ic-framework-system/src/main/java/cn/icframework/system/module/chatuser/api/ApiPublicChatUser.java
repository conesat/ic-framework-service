package cn.icframework.system.module.chatuser.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2025/01/17
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/chat-user", name ="聊天用户关联")
public class ApiPublicChatUser extends BasicApi {

}
