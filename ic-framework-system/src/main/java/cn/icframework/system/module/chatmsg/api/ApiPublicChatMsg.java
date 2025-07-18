package cn.icframework.system.module.chatmsg.api;

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
@RequestMapping(value = Api.API_PUBLIC + "/chat-msg", name ="消息")
public class ApiPublicChatMsg extends BasicApi {

}
