package cn.icframework.hotel.module.scheduling.pojo.vo;

import cn.icframework.mybatis.annotation.Collection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ic
 * @date 2024/11/26
 */
@Getter
@Setter
public class SchedulingGroupVO {
    /**
     * 用户
     */
    private String userId;
    private String userName;

    @Collection(groupMainId = "user_id")
    private List<SchedulingVO> schedulingList;
}
