package cn.icframework.system.module.menu.service;

import cn.icframework.common.enums.Status;
import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.menu.Menu;
import cn.icframework.system.module.menu.dao.MenuMapper;
import cn.icframework.system.module.menu.def.MenuDef;
import cn.icframework.system.module.menu.pojo.dto.MenuDTO;
import cn.icframework.system.module.menu.pojo.vo.MenuVO;
import cn.icframework.system.module.menu.pojo.vo.MenuWithChildrenVO;
import cn.icframework.system.module.menu.wrapperbuilder.MenuWrapperBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ic
 * @since 2024/08/20
 */
@Service
@RequiredArgsConstructor
public class MenuService extends BasicService<MenuMapper, Menu> {
    private final MenuWrapperBuilder wrapperBuilder;

    /**
     * 编辑或者保存，并且移除缓存
     *
     * @param dto
     */
    @Transactional
    public void edit(MenuDTO dto) {
        Menu entity = dto.getId() != null ? selectById(dto.getId()) : new Menu();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity, true);
        } else {
            insert(entity);
        }
    }


    @Override
    public void before(SqlCommandType sqlCommandType, Menu entity) {
        // 存在上级的去掉/开头
        if (entity.getParentId() != null && entity.getPath().startsWith("/")) {
            entity.setPath(entity.getPath().substring(1));
        }
        if (sqlCommandType == SqlCommandType.UPDATE) {
            checkPath(entity);
        } else if (sqlCommandType == SqlCommandType.DELETE) {
            Assert.isFalse(entity.getSystem(), "系统菜单不允许删除");
        }
    }

    @Transactional
    @Override
    public void after(SqlCommandType sqlCommandType, Menu entity) {
        MenuDef menuDef = MenuDef.table();
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            String newPath = getPath(entity);
            if (!Objects.equals(newPath, entity.getParentPath())) {
                // 更新路径，跳过前后置处理
                update(UPDATE(menuDef).SET(menuDef.parentPath, newPath).WHERE(menuDef.id.eq(entity.getId())));
            }
        } else if (sqlCommandType == SqlCommandType.DELETE) {
            // 移除上级关联
            update(UPDATE(MenuDef.table()).SET(menuDef.parentId.set(null)).WHERE(menuDef.parentId.eq(entity.getId())));
        }
    }


    /**
     * 校验上级不能为自己或者自己的下级
     *
     * @param entity
     */
    private void checkPath(Menu entity) {
        if (entity.getParentId() == null) {
            return;
        }
        Menu parent = selectById(entity.getParentId());
        if (parent == null || !StringUtils.hasLength(parent.getParentPath())) {
            return;
        }
        Set<String> pathIds = Arrays.stream(parent.getParentPath().split(":")).collect(Collectors.toSet());
        Assert.isTrue(!pathIds.contains(String.valueOf(entity.getId())), "上级不能为自己或者自己的下级");
    }

    /**
     * 获取路径
     *
     * @param entity
     * @return
     */
    private String getPath(Menu entity) {
        String parentPath;
        if (entity.getParentId() == null) {
            return null;
        }
        Menu parent = selectById(entity.getParentId());
        if (parent == null) {
            return null;
        }
        parentPath = parent.getParentPath();
        if (StringUtils.hasLength(parentPath)) {
            List<String> pathIds = Arrays.stream(parentPath.split(":")).collect(Collectors.toList());
            pathIds.add(String.valueOf(parent.getId()));
            return String.join(":", pathIds);
        } else {
            return String.valueOf(parent.getId());
        }
    }

    public MenuVO detail(Serializable id) {
        return selectOne(wrapperBuilder.detail(id), MenuVO.class);
    }

    @Cacheable(cacheNames = "menu:all", key = "(#menuPlatformType != null ? #menuPlatformType : 'default') + (#userType != null  ? #userType:'default')")
    public List<MenuWithChildrenVO> allWithCache(Integer[] menuPlatformType,
                                                 String userType) {
        QueryParams queryParams = new QueryParams();
        queryParams.put("userType", userType);
        queryParams.put("menuPlatformType", menuPlatformType);
        queryParams.put("status", Status.ENABLE.code());
        queryParams.put("parentIsNull", true);
        SqlWrapper sqlWrapper = wrapperBuilder.build(queryParams);
        return select(sqlWrapper, MenuWithChildrenVO.class);
    }

    /**
     * 移除缓存
     */
    @CacheEvict(cacheNames = {"menu:all", "menu"}, allEntries = true)
    public void removeAllCache() {
    }
}
