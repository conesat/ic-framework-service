package cn.icframework.hotel.module.roompic.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/10/10
 */
@Getter
@Setter
public class RoomPicVO {
    private Long id;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 文件url
     */
    private String fileUrl;
    private Boolean mainPic;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
