package cn.icframework.hotel.module.serverorder.pojo.vo;

import cn.icframework.hotel.common.enums.ServerStateEnum;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/11/29
 */
@Getter
@Setter
public class ServerOrderVO {
    private Long id;
    /**
     * 接单人员id
     */
    private Long serverUserId;
    private String serverUserAvatarUrl;
    private String serverUserName;
    private String targetContent;
    /**
     * 房间订单id
     */
    private Long roomOrderId;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 房间号
     */
    private String roomNo;
    /**
     * 服务状态
     */
    private ServerStateEnum state;
    /**
     * 服务类型[1=清洁][2=送货上门]
     */
    private ServerTargetEnum target;
    /**
     * 服务内容id
     */
    private Long targetId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;
    /**
     * 接单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime acceptTime;
    /**
     * 预定处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime doTime;
}
