package cn.icframework.hotel.module.hoteluser.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.hoteluser.HotelUser;
import cn.icframework.hotel.module.hoteluser.dao.HotelUserMapper;
import cn.icframework.hotel.module.hoteluser.pojo.dto.HotelUserDTO;
import cn.icframework.system.module.sysfile.service.SysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author ic
 * @date 2024/10/25
 */
@Service
@RequiredArgsConstructor
public class HotelUserService extends BasicService<HotelUserMapper, HotelUser> {
    private final SysFileService sysFileService;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(HotelUserDTO dto) {
        HotelUser entity = dto.getId() != null ? selectById(dto.getId()) : HotelUser.def();
        Long avatarFileIdOld = entity.getAvatarFileId();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
            if (avatarFileIdOld != null && !Objects.equals(avatarFileIdOld, entity.getAvatarFileId())) {
                sysFileService.removeRef(avatarFileIdOld);
                if (entity.getAvatarFileId() != null) {
                    sysFileService.addRef(entity.getAvatarFileId());
                }
            }
        } else {
            insert(entity);
            if (entity.getAvatarFileId() != null) {
                sysFileService.addRef(entity.getAvatarFileId());
            }
        }
    }


}
