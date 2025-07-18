package cn.icframework.system.module.sysfile.pojo.vo;

import cn.icframework.system.enums.FileType;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author create by ic gen
 * @since 2023/06/21
 */
@Getter
@Setter
public class SysFileVO {
    private Long id;
    /**
     * 文件大小
     */
    private Long size;
    private String sizeTip;
    /**
     * ossObjectName
     */
    private String ossObjectName;
    /**
     * 文件名称
     */
    private String name;
    /**
     * bucketUrl
     */
    private String bucketUrl;
    private String url;
    /**
     * 引用次数
     */
    private Integer refCount;
    /**
     * 文件系统类型
     */
    private FileType type;

    /**
     * 所属用户
     */
    private Long userId;
    private String userName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
