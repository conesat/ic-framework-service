package cn.icframework.system.module.sysfile.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author create by ic gen
 * @since 2023/06/21
 */
@Getter
@Setter
public class SysFileDTO {
    private Long id;
    /**
     * 文件大小
     */
    @NotNull
    private Long size;
    /**
     * ossObjectName
     */
    private String ossObjectName;
    /**
     * 文件名称
     */
    @NotNull
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
    @NotNull
    private Integer type;
}
