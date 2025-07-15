package cn.icframework.hotel.module.consumeitem.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ic
 * @date 2024/10/14
 */
@Getter
@Setter
public class ConsumeItemVO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 价格
     */
    private Double price;
    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 文件url
     */
    private String fileUrl;

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
