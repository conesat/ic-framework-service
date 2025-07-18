package cn.icframework.system.module.dic.service;

import cn.icframework.common.enums.Status;
import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.dic.Dic;
import cn.icframework.system.module.dic.dao.DicMapper;
import cn.icframework.system.module.dic.pojo.dto.DicDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author create by ic gen
 * @since 2023/06/14
 */
@Service
public class DicService extends BasicService<DicMapper, Dic> {

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(DicDTO dto) {
        Dic entity = dto.getId() != null ? selectById(dto.getId()) : new Dic();
        BeanUtils.copyExcludeProps(dto, entity, Dic::getCreateTime);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    public void changeEnable(List<Serializable> ids, Boolean enable) {
        for (Serializable id : ids) {
            Dic dic = selectById(id);
            if (dic != null) {
                dic.setStatus(enable ? Status.ENABLE : Status.DISABLE);
                updateById(dic);
            }
        }
    }
}
