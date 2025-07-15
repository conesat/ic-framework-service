package cn.icframework.system.module.permission.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.system.module.permission.Permission;
import cn.icframework.system.module.permission.dao.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author create by ic gen
 * @date 2023/06/20
 */
@Service
@RequiredArgsConstructor
public class PermissionService extends BasicService<PermissionMapper, Permission> {

}
