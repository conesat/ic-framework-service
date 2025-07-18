package cn.icframework.system.module.notice.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2024/09/12
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/notice", name ="通知")
public class ApiPublicNotice extends BasicApi {

}
