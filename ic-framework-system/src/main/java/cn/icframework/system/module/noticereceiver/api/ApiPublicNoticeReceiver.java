package cn.icframework.system.module.noticereceiver.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2024/09/13
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/notice-receiver", name ="通知接收对象")
public class ApiPublicNoticeReceiver extends BasicApi {

}
