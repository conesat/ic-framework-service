package cn.icframework.hotel.module.roompic;

import cn.icframework.mybatis.annotation.ForeignKey;
import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.annotation.ForeignKeyAction;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.module.room.Room;
import cn.icframework.system.module.sysfile.SysFile;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "hotel_room_pic", comment = "房间图片")
public class RoomPic {

    @Id(idType = IdType.SNOWFLAKE)
    private Long id;

    /**
    * 房间id
    */
    @ForeignKey(references = Room.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "room_id", comment = "房间id")
    private Long roomId;

    /**
    * 文件id
    */
    @ForeignKey(references = SysFile.class, onDelete = ForeignKeyAction.CASCADE)
    @TableField(value = "file_id", comment = "文件id")
    private Long fileId;

    /**
    * 文件url
    */
    @TableField(value = "file_url", comment = "文件url")
    private String fileUrl;

    /**
    * 是否主图
    */
    @TableField(value = "main_pic", defaultValue = "0", comment = "是否主图")
    private Boolean mainPic;

    /**
    * 创建时间
    */
    @TableField(value = "create_time", notNull = true, comment = "创建时间", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    @TableField(value = "update_time", comment = "更新时间", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static RoomPic def() {
        RoomPic def = new RoomPic();
        return def;
    }
}
