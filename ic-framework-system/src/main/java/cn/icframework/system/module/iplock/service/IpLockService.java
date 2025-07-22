package cn.icframework.system.module.iplock.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.core.utils.IpUtils;
import cn.icframework.core.utils.LocalDateTimeUtils;
import cn.icframework.system.module.iplock.IpLock;
import cn.icframework.system.module.iplock.pojo.dto.IpLockDTO;
import cn.icframework.system.module.iplock.dao.IpLockMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author ic
 * @since 2025/07/22
 */
@Service
@RequiredArgsConstructor
public class IpLockService extends BasicService<IpLockMapper, IpLock> {

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(IpLockDTO dto) {
        IpLock entity = dto.getIp() != null ? selectById(dto.getIp()) : new IpLock();
        BeanUtils.copyExcludeProps(dto, entity);
        if (StringUtils.hasLength(dto.getIp())) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    /**
     * 判断是否需要验证码
     */
    public boolean checkNeedCaptcha(HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        IpLock ipLock = selectById(ipAddress);
        return ipLock != null && ipLock.getLoginFailCount() >= 3 && ipLock.getLockEndTime().isAfter(LocalDateTime.now());
    }
}
