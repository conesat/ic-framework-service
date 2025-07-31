package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传帮助类
 * 用于处理文件上传的相关操作
 *
 * @author ic
 */
public interface IFileHelper {
    /**
     * 获取分配上传uploadId
     *
     * @param useType    使用类型
     * @param fileName   文件名
     * @param totalParts 总分片数
     * @param userId     用户ID
     * @return 上传ID
     */
    String registerUploadSlice(FileUseType useType, String fileName, int totalParts, Long userId);

    /**
     * 分片上传
     *
     * @param file       文件
     * @param uploadId   上传ID
     * @param partNumber 分片编号
     * @return 文件信息
     */
    SysFile uploadSlice(MultipartFile file, String uploadId, int partNumber);

    /**
     * 取消上传分片
     *
     * @param uploadId 上传ID
     */
    void abortUploadSlice(String uploadId);

    /**
     * 单文件上传
     *
     * @param file    文件
     * @param useType 使用类型
     * @param userId  用户ID
     * @return 文件信息
     */
    SysFile uploadSingle(MultipartFile file, FileUseType useType, Long userId);
}
