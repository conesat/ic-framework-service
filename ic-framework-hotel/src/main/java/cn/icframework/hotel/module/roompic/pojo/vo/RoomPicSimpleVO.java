package cn.icframework.hotel.module.roompic.pojo.vo;

import lombok.Getter;
import lombok.Setter;


/**
 * @author ic
 * @date 2024/10/10
 */
@Getter
@Setter
public class RoomPicSimpleVO {
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 文件url
     */
    private String fileUrl;
    private Boolean mainPic;
}
