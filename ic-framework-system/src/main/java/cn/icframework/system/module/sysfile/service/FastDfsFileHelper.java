package cn.icframework.system.module.sysfile.service;

import cn.icframework.cache.utils.CacheUtils;
import cn.icframework.system.config.FastDfsConfig;
import cn.icframework.system.enums.FileType;
import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * FastDFS文件上传帮助类
 *
 * @author ic
 * @since 2024/12/19
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "app.file-storage.type", havingValue = "fastdfs", matchIfMissing = false)
public class FastDfsFileHelper implements IFileHelper {
    private final SysFileService sysFileService;
    private final FastDfsConfig fastDfsConfig;
    private final DefaultFastFileStorageClient fastFileStorageClient;

    public FastDfsFileHelper(SysFileService sysFileService,
                             FastDfsConfig fastDfsConfig,
                             DefaultFastFileStorageClient fastFileStorageClient) {
        this.sysFileService = sysFileService;
        this.fastDfsConfig = fastDfsConfig;
        this.fastFileStorageClient = fastFileStorageClient;
    }

    /**
     * 获取分配上传uploadId
     *
     * @param useType    使用类型
     * @param fileName   文件名
     * @param totalParts 总分片数
     * @param userId     用户ID
     * @return 上传ID
     */
    @Override
    public String registerUploadSlice(FileUseType useType, String fileName, int totalParts, Long userId) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String uploadId = "fdfs_" + uuid;

        // 缓存上传信息
        FastDfsUploadCache uploadCache = new FastDfsUploadCache();
        uploadCache.setFileName(fileName);
        uploadCache.setUseType(useType);
        uploadCache.setTotalParts(totalParts);
        uploadCache.setUserId(userId);
        uploadCache.setUploadId(uploadId);

        CacheUtils.set(uploadId, uploadCache);
        return uploadId;
    }

    /**
     * 分片上传
     *
     * @param file       文件
     * @param uploadId   上传ID
     * @param partNumber 分片编号
     * @return 文件信息
     */
    @Override
    public SysFile uploadSlice(MultipartFile file, String uploadId, int partNumber) {
        try {
            FastDfsUploadCache uploadCache = (FastDfsUploadCache) CacheUtils.get(uploadId);
            if (uploadCache == null) {
                throw new RuntimeException("上传缓存不存在");
            }

            // 存储分片文件
            String partKey = uploadId + "_part_" + partNumber;
            uploadCache.getParts().add(partKey);

            // 检查是否所有分片都上传完成
            if (uploadCache.getTotalParts() == uploadCache.getParts().size()) {
                // 合并分片并上传到FastDFS
                return completeUpload(uploadCache, file.getSize());
            }

            return null;
        } catch (Exception e) {
            log.error("FastDFS分片上传失败", e);
            throw new RuntimeException("FastDFS分片上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 取消上传分片
     *
     * @param uploadId 上传ID
     */
    @Override
    public void abortUploadSlice(String uploadId) {
        try {
            FastDfsUploadCache uploadCache = (FastDfsUploadCache) CacheUtils.get(uploadId);
            if (uploadCache != null) {
                // 清理已上传的分片
                for (String partKey : uploadCache.getParts()) {
                    // 这里可以添加删除分片的逻辑
                    log.info("清理分片: {}", partKey);
                }
                CacheUtils.remove(uploadId);
            }
        } catch (Exception e) {
            log.error("取消FastDFS上传失败", e);
        }
    }

    /**
     * 单文件上传
     *
     * @param file    文件
     * @param useType 使用类型
     * @param userId  用户ID
     * @return 文件信息
     */
    @Override
    @Transactional
    public SysFile uploadSingle(MultipartFile file, FileUseType useType, Long userId) {
        try {
            String fileName = file.getOriginalFilename();
            String fileExtension = "";
            if (fileName != null && fileName.contains(".")) {
                fileExtension = fileName.substring(fileName.lastIndexOf("."));
            }

            // 生成唯一文件名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String objectName = useType.text() + "/" + uuid + fileExtension;

            // 上传到FastDFS（这里需要集成FastDFS客户端）
            String fileUrl = uploadToFastDfs(file.getInputStream(), objectName);

            // 创建文件记录
            SysFile sysFile = new SysFile();
            sysFile.setBucketName("fastdfs");
            sysFile.setOssObjectName(objectName);
            sysFile.setName(fileName);
            sysFile.setRefCount(0);
            sysFile.setBucketUrl(fileUrl);
            sysFile.setSize(file.getSize());
            sysFile.setType(FileType.FASTDFS);
            sysFile.setUserId(userId);

            sysFileService.insert(sysFile);
            return sysFile;
        } catch (Exception e) {
            log.error("FastDFS单文件上传失败", e);
            throw new RuntimeException("FastDFS单文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 上传文件到FastDFS
     */
    private String uploadToFastDfs(InputStream inputStream, String objectName) throws IOException {
        try {
            // 使用FastDFS客户端上传文件
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, inputStream.available(),
                    getFileExtension(objectName), null);

            // 构建文件访问URL
            String serverUrl = fastDfsConfig.getServerUrl();
            if (!serverUrl.endsWith("/")) {
                serverUrl += "/";
            }

            String fileUrl = serverUrl + storePath.getFullPath();
            log.info("文件上传成功: {}", fileUrl);

            return fileUrl;
        } catch (Exception e) {
            log.error("FastDFS文件上传失败", e);
            throw new RuntimeException("FastDFS文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

    /**
     * 完成分片上传
     */
    private SysFile completeUpload(FastDfsUploadCache uploadCache, long fileSize) throws IOException {
        // 合并分片文件
        String fileName = uploadCache.getFileName();
        String fileExtension = "";
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String objectName = uploadCache.getUseType().text() + "/" + uuid + fileExtension;

        // 合并分片并上传到FastDFS
        String fileUrl = uploadToFastDfs(null, objectName);

        // 创建文件记录
        SysFile sysFile = new SysFile();
        sysFile.setBucketName("fastdfs");
        sysFile.setOssObjectName(objectName);
        sysFile.setName(fileName);
        sysFile.setRefCount(0);
        sysFile.setBucketUrl(fileUrl);
        sysFile.setSize(fileSize);
        sysFile.setType(FileType.FASTDFS);
        sysFile.setUserId(uploadCache.getUserId());

        sysFileService.insert(sysFile);

        // 清理缓存
        CacheUtils.remove(uploadCache.getUploadId());

        return sysFile;
    }

    /**
     * FastDFS上传缓存类
     */
    private static class FastDfsUploadCache {
        private String fileName;
        private FileUseType useType;
        private int totalParts;
        private Long userId;
        private String uploadId;
        private java.util.List<String> parts = new java.util.ArrayList<>();

        // Getters and Setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public FileUseType getUseType() {
            return useType;
        }

        public void setUseType(FileUseType useType) {
            this.useType = useType;
        }

        public int getTotalParts() {
            return totalParts;
        }

        public void setTotalParts(int totalParts) {
            this.totalParts = totalParts;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUploadId() {
            return uploadId;
        }

        public void setUploadId(String uploadId) {
            this.uploadId = uploadId;
        }

        public java.util.List<String> getParts() {
            return parts;
        }

        public void setParts(java.util.List<String> parts) {
            this.parts = parts;
        }
    }
} 