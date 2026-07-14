package cn.icframework.project.module.energy.repairorder.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.project.module.energy.repairorder.RepairOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * public
 * 用于公开内容
 *
 * @author ic
 * @since 2026/07/13
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/repair-order", name ="维修工单")
public class ApiPublicRepairOrder extends BasicApi {

}
