package cn.icframework.hotel.module.roompic.pojo.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ic
 * @date 2024/10/10
 */
@Getter
@Setter
public class RoomPicDTO {
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

}
