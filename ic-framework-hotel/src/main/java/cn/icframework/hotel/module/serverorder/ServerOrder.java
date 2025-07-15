package cn.icframework.hotel.module.serverorder;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.common.enums.ServerStateEnum;
import cn.icframework.hotel.common.enums.ServerTargetEnum;
import cn.icframework.system.module.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_server_order", comment = "服务订单")
public class ServerOrder {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 接单人员id
    */
    @ForeignKey(references = User.class)
    @TableField(value = "server_user_id", comment = "接单人员id")
    private Long serverUserId;

    /**
    * 服务状态
    */
    @TableField(value = "state", comment = "服务状态")
    private ServerStateEnum state;

    /**
    * 服务类型
    */
    @TableField(value = "target", comment = "服务类型[1=清洁][2=送货上门]")
    private ServerTargetEnum target;

    /**
    * target内容
    */
    @TableField(value = "target_content", comment = "target内容")
    private String targetContent;

    /**
    * 服务内容id
    */
    @TableField(value = "target_id", comment = "服务内容id")
    private Long targetId;

    /**
    * 创建时间
    */
    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
    * 完成时间
    */
    @TableField(value = "finish_time", comment = "完成时间")
    private LocalDateTime finishTime;

    /**
    * 接单时间
    */
    @TableField(value = "accept_time", comment = "接单时间")
    private LocalDateTime acceptTime;

    /**
    * 指定时间
    */
    @TableField(value = "do_time", comment = "指定时间")
    private LocalDateTime doTime;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static ServerOrder def() {
        ServerOrder def = new ServerOrder();
        return def;
    }
}
