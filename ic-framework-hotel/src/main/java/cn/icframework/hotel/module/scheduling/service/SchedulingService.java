package cn.icframework.hotel.module.scheduling.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.hotel.module.scheduling.Scheduling;
import cn.icframework.hotel.module.scheduling.dao.SchedulingMapper;
import cn.icframework.hotel.module.scheduling.def.SchedulingDef;
import cn.icframework.hotel.module.scheduling.pojo.dto.SchedulingDTO;
import cn.icframework.hotel.module.scheduling.pojo.dto.SchedulingQueryDTO;
import cn.icframework.hotel.module.scheduling.pojo.vo.SchedulingGroupVO;
import cn.icframework.hotel.module.scheduling.pojo.vo.SchedulingVO;
import cn.icframework.hotel.module.timescope.def.TimeScopeDef;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.module.depuser.def.DepUserDef;
import cn.icframework.system.module.user.def.UserDef;
import cn.icframework.system.module.userpos.def.UserPosDef;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ic
 * @date 2024/11/26
 */
@Service
public class SchedulingService extends BasicService<SchedulingMapper, Scheduling> {

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(SchedulingDTO dto) {
        if (dto.getId() != null) {
            Scheduling entity = selectById(dto.getId());
            BeanUtils.copyExcludeProps(dto, entity);
            updateById(entity);
            return;
        }
        // 移除掉之前排过的且冲突的班次
        SchedulingDef schedulingDef = SchedulingDef.table();
        delete(DELETE().FROM(schedulingDef).WHERE(schedulingDef.date.ge(dto.getDateStart()).date.le(dto.getDateEnd()).userId.in(dto.getUserIds())));

        // 遍历所有日期生成排班
        LocalDate localDate = dto.getDateStart();
        List<Scheduling> schedulingList = new ArrayList<>();
        while (!localDate.isAfter(dto.getDateEnd())) {
            for (Long userId : dto.getUserIds()) {
                Scheduling entity = Scheduling.def();
                entity.setDate(localDate);
                entity.setUserId(userId);
                entity.setTimeScopeId(dto.getTimeScopeId());
                schedulingList.add(entity);
            }
            localDate = localDate.plusDays(1);
        }
        insertBatch(schedulingList);
    }

    /**
     * 分页查询人员排班信息
     *
     * @param queryDTO
     * @param page
     * @return
     */
    public PageResponse<SchedulingGroupVO> pageGroup(SchedulingQueryDTO queryDTO, PageRequest page) {
        SchedulingDef schedulingDef = SchedulingDef.table();
        UserDef userDef = UserDef.table();
        // 先构建人员分页def
        SqlWrapper pageUserWrapper = SELECT().FROM(schedulingDef)
                .INNER_JOIN(userDef).ON(userDef.id.eq(schedulingDef.userId))
                .WHERE(schedulingDef.date.ge(queryDTO.getDateStart()).date.le(queryDTO.getDateEnd()));
        if (queryDTO.getTimeScopeId() != null) {
            pageUserWrapper = pageUserWrapper.WHERE(schedulingDef.timeScopeId.eq(queryDTO.getTimeScopeId()));
        }
        if (queryDTO.getSign() != null) {
            pageUserWrapper = pageUserWrapper.WHERE(schedulingDef.signInTime.notNull());
        }
        if (queryDTO.getDeptId() != null) {
            DepUserDef depUserDef = DepUserDef.table();
            pageUserWrapper = pageUserWrapper.INNER_JOIN(depUserDef).ON(depUserDef.userId.eq(userDef.id)).WHERE(depUserDef.depId.eq(queryDTO.getDeptId()));
        }
        if (queryDTO.getPosId() != null) {
            UserPosDef userPosDef = UserPosDef.table();
            pageUserWrapper = pageUserWrapper.INNER_JOIN(userPosDef).ON(userPosDef.userId.eq(userDef.id)).WHERE(userPosDef.positionId.eq(queryDTO.getPosId()));
        }

        // 构建一个子查询
        SchedulingDef userPageDef = pageUserWrapper.SELECT(schedulingDef.userId)
                .GROUP_BY(schedulingDef.userId)
                .PAGE(page)
                .AS(SchedulingDef.class)
                .as("user_page");

        // 用子查询重新join一下
        TimeScopeDef timeScopeDef = TimeScopeDef.table();
        SqlWrapper sqlWrapper = pageUserWrapper.SELECT(schedulingDef._all,
                        timeScopeDef.name.as(SchedulingVO::getTimeScopeName),
                        timeScopeDef.startTime.as(SchedulingVO::getTimeScopeStartTime),
                        timeScopeDef.endTime.as(SchedulingVO::getTimeScopeEndTime),
                        userDef.name.as(SchedulingGroupVO::getUserName))
                .INNER_JOIN(userPageDef).ON(userPageDef.userId.eq(schedulingDef.userId))
                .INNER_JOIN(timeScopeDef).ON(timeScopeDef.id.eq(schedulingDef.timeScopeId));
        List<SchedulingGroupVO> schedulingGroupVO = select(sqlWrapper, SchedulingGroupVO.class);
        return new PageResponse<>(page, schedulingGroupVO);
    }

    /**
     * 批量删除职工排班
     * @param userIds
     */
    public void deleteByUserIds(List<Serializable> userIds) {
        SchedulingDef schedulingDef = SchedulingDef.table();
        delete(DELETE().FROM(schedulingDef).WHERE(schedulingDef.userId.in(userIds).date.ge(LocalDate.now())));
    }
}
