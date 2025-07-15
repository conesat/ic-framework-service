package cn.icframework.system.module.permissiongroup.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.permission.wrapperbuilder.PermissionWrapperBuilder;
import cn.icframework.system.module.permissiongroup.PermissionGroup;
import cn.icframework.system.module.permissiongroup.dao.PermissionGroupMapper;
import cn.icframework.system.module.permissiongroup.pojo.dto.PermissionGroupDTO;
import cn.icframework.system.module.permissiongroup.pojo.vo.PermissionGroupVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ic
 * @date 2024/08/02
 */
@Service
@RequiredArgsConstructor
public class PermissionGroupService extends BasicService<PermissionGroupMapper, PermissionGroup> {
    private final PermissionWrapperBuilder wrapperBuilder;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(PermissionGroupDTO dto) {
        PermissionGroup entity = dto.getId() != null ? selectById(dto.getId()) : PermissionGroup.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() == null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Cacheable(cacheNames = "permission-group:all")
    public List<PermissionGroupVO> all(String userType) {
        return select(wrapperBuilder.allWithGroup(userType), PermissionGroupVO.class);
    }


    /**
     * 移除缓存
     */
    @CacheEvict(value = "permission-group:all", allEntries = true)
    public void removeAllCache() {
    }
}
