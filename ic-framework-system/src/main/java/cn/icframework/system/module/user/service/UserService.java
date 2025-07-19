package cn.icframework.system.module.user.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.core.utils.RsaUtils;
import cn.icframework.system.common.RegisterLoginHelper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.enums.MenuPlatformType;
import cn.icframework.system.module.depuser.service.DepUserService;
import cn.icframework.system.module.menu.pojo.vo.MenuWithChildrenVO;
import cn.icframework.system.module.rolemenu.service.RoleMenuService;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.service.SysFileService;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.dao.UserMapper;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.user.pojo.dto.UserDTO;
import cn.icframework.system.module.user.pojo.dto.UserMineDTO;
import cn.icframework.system.module.userpos.service.UserPosService;
import cn.icframework.system.module.userrole.def.UserRoleDef;
import cn.icframework.system.module.userrole.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Objects;


/**
 * @author hzl
 * @since 2023/5/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends BasicService<UserMapper, User> {

    private final RegisterLoginHelper registerLoginHelper;
    private final UserRoleService userRoleService;
    private final DepUserService depUserService;
    private final UserPosService userPosService;
    private final RoleMenuService roleMenuService;
    private final SysFileService sysFileService;
    private IUserInfoProvider userInfoProvider;

    @Autowired(required = false)
    public void setUserInfoProvider(IUserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;
    }

    /**
     * 初始化超管
     */
    @Transactional
    public void initSu(UserDTO dto) {
        User entity = new User();
        BeanUtils.copyExcludeProps(dto, entity, UserDTO::getPasswd, UserDTO::getStatus, UserDTO::getId);
        entity.setSu(true);
        entity.setPasswd(registerLoginHelper.decryptPassWd(UserType.SYSTEM_USER, dto.getUsername(), dto.getPasswd()));
        insert(entity);
        userRoleService.setUserRole(entity.getId(), dto.getRoleIds(), false);
    }

    /**
     * 编辑账号
     */
    @Transactional
    public void edit(UserDTO dto) {
        // 判断冲突
        long count = count(UserDef.table().id.ne(dto.getId()).username.eq(dto.getUsername()));
        Assert.is0(count, "账号已存在");
        User entity = dto.getId() != null ? selectById(dto.getId()) : new User();

        Long oldAvatarFileId = entity.getAvatarFileId();
        boolean su = entity.getSu() != null && entity.getSu();
        if (su) {
            BeanUtils.copyExcludeProps(dto, entity, UserDTO::getPasswd, UserDTO::getStatus, UserDTO::getId);
        } else {
            BeanUtils.copyExcludeProps(dto, entity, UserDTO::getPasswd, UserDTO::getId);
        }

        // 新增或者头像变化调整地址
        if (dto.getId() == null || !Objects.equals(oldAvatarFileId, entity.getAvatarFileId())) {
            SysFile sysFile = sysFileService.selectById(entity.getAvatarFileId());
            if (sysFile != null) {
                entity.setAvatarFileUrl(sysFile.getBucketUrl() + "/" + sysFile.getOssObjectName());
            }
        }

        boolean update = false;
        if (dto.getId() != null) {
            update = true;
            entity.setUsername(null);
            updateById(entity);
        } else {
            entity.setPasswd(registerLoginHelper.decryptPassWd(UserType.SYSTEM_USER, dto.getUsername(), dto.getPasswd()));
            insert(entity);
        }
        if (!su) {
            userRoleService.setUserRole(entity.getId(), dto.getRoleIds(), update);
        }
        depUserService.setUserDep(entity.getId(), dto.getDepIds(), update);
        userPosService.setUserPos(entity.getId(), dto.getPosIds(), update);

        if (!Objects.equals(oldAvatarFileId, entity.getAvatarFileId())) {
            sysFileService.removeRef(oldAvatarFileId);
            if (entity.getAvatarFileId() != null) {
                sysFileService.addRef(entity.getAvatarFileId());
            }
        }
    }

    /**
     * 编辑个人信息
     *
     * @param userId
     * @param dto
     */
    public void editMine(Long userId, UserMineDTO dto) {
        User entity = selectById(userId);
        BeanUtils.copyExcludeProps(dto, entity);
        updateById(entity);
    }

    /**
     * 获取菜单
     *
     * @param userId
     * @return
     */
    public List<MenuWithChildrenVO> getMenu(Long userId) {
        boolean su = userRoleService.isSu(userId);
        if (su) {
            return roleMenuService.getMenu(MenuPlatformType.SYS);
        }
        return roleMenuService.getMenu(userId, MenuPlatformType.SYS);
    }


    @Transactional
    public void editAvatar(Long userId, Long avatarFileId) {
        User user = selectById(userId);
        if (Objects.equals(user.getAvatarFileId(), avatarFileId)) {
            return;
        }
        sysFileService.removeRef(user.getAvatarFileId());

        // 新增或者头像变化调整地址
        SysFile sysFile = sysFileService.selectById(avatarFileId);
        if (sysFile != null) {
            UserDef userDef = UserDef.table();
            update(UPDATE(userDef).SET(userDef.avatarFileId.set(avatarFileId).avatarFileUrl.set(sysFile.getBucketUrl() + "/" + sysFile.getOssObjectName())).WHERE(userDef.id.eq(userId)));
            sysFileService.addRef(avatarFileId);
        }
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param passwd
     * @param passwdOld
     */
    public void updatePasswd(Long userId, String passwd, String passwdOld) {
        User user = selectById(userId);
        String key = registerLoginHelper.getCacheKey(UserType.SYSTEM_USER, user.getUsername());
        try {
            String decPasswd = RsaUtils.privateDecrypt(passwd, RsaUtils.getPrivateKey(key));
            String decPasswdOld = RsaUtils.privateDecrypt(passwdOld, RsaUtils.getPrivateKey(key));
            Assert.isTrue(decPasswdOld.equals(user.getPasswd()), "旧密码错误");
            Assert.isFalse(decPasswd.equals(decPasswdOld), "新密码不能与旧密码一致");
            User update = new User();
            update.setId(user.getId());
            update.setPasswd(decPasswd);
            updateById(update);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void after(SqlCommandType sqlCommandType, User entity) {
        if (sqlCommandType == SqlCommandType.DELETE) {
            userRoleService.delete(UserRoleDef.table().userId.eq(entity.getId()));
            if (entity.getAvatarFileId() != null) {
                sysFileService.removeRef(entity.getAvatarFileId());
            }
        }
    }

    @Override
    public void before(SqlCommandType sqlCommandType, User entity) {
        if (sqlCommandType == SqlCommandType.DELETE) {
            // 超管不允许删除
            Assert.isFalse(entity.getSu(), "超管不允许删除");
        }
    }

    /**
     * 获取外部用户信息
     *
     * @param userType
     * @param userId
     * @return
     */
    public IUserInfoProvider.BaseUserInfo getUserInfo(String userType, String userId) {
        if (Objects.equals(userType, cn.icframework.system.enums.UserType.SYSTEM_USER.code())) {
            // 系统用户使用userService查询
            User user = selectById(userId);
            if (user == null) {
                return null;
            }
            IUserInfoProvider.BaseUserInfo baseUserInfo = new IUserInfoProvider.BaseUserInfo();
            baseUserInfo.setUserId(userId);
            baseUserInfo.setUserType(userType);
            baseUserInfo.setUserName(user.getName());
            baseUserInfo.setUserPic(user.getAvatarFileUrl());
            return baseUserInfo;
        } else if (userInfoProvider != null) {
            return userInfoProvider.getInfo(userType, userId);
        }
        throw new RuntimeException("非系统用户，请实现IUserInfoProvider来获取用户信息");
    }

}
