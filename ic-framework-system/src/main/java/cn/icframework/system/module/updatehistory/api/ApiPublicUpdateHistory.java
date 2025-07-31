package cn.icframework.system.module.updatehistory.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2025/07/31
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/update-history", name ="更新历史")
public class ApiPublicUpdateHistory extends BasicApi {

}
