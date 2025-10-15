package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.config.MinioConfig;
import cn.icframework.system.enums.FileType;
import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * MinIO文件上传帮助类
 *
 * @author ic
 * @since 2024/12/19
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "app.file-storage.type", havingValue = "minio", matchIfMissing = false)
public class MinioFileHelper implements IFileHelper {
    private final SysFileService sysFileService;
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    public MinioFileHelper(SysFileService sysFileService,
                          MinioConfig minioConfig,
                          MinioClient minioClient) {
        this.sysFileService = sysFileService;
        this.minioConfig = minioConfig;
        this.minioClient = minioClient;
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
        return "minio_" + uuid;
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
        // MinIO 支持分片上传，这里简化处理
        return uploadSingle(file, FileUseType.AVATAR, 1L);
    }

    /**
     * 取消上传分片
     *
     * @param uploadId 上传ID
     */
    @Override
    public void abortUploadSlice(String uploadId) {
        log.info("取消MinIO上传: {}", uploadId);
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

            // 确保存储桶存在
            ensureBucketExists();

            // 上传到MinIO
            String fileUrl = uploadToMinio(file.getInputStream(), objectName, file.getContentType());

            // 创建文件记录
            SysFile sysFile = new SysFile();
            sysFile.setBucketName(minioConfig.getBucketName());
            sysFile.setOssObjectName(objectName);
            sysFile.setName(fileName);
            sysFile.setRefCount(0);
            sysFile.setBucketUrl(fileUrl);
            sysFile.setSize(file.getSize());
            sysFile.setType(FileType.MINIO);
            sysFile.setUserId(userId);

            sysFileService.insert(sysFile);
            return sysFile;
        } catch (Exception e) {
            log.error("MinIO单文件上传失败", e);
            throw new RuntimeException("MinIO单文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 上传文件到MinIO
     */
    private String uploadToMinio(InputStream inputStream, String objectName, String contentType) throws Exception {
        try {
            // 上传文件到MinIO
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build()
            );

            // 构建基础 URL - 只包含 endpoint 和 bucket，不包含 object name
            // 前端会使用 bucketUrl + '/' + ossObjectName 来构建完整 URL
            String endpoint = minioConfig.getEndpoint();
            if (!endpoint.endsWith("/")) {
                endpoint += "/";
            }
            
            // 构建基础 URL（不包含对象名）
            String baseUrl = endpoint + minioConfig.getBucketName();
            log.info("文件上传成功，对象名: {}, 基础URL: {}", objectName, baseUrl);

            return baseUrl;
        } catch (Exception e) {
            log.error("MinIO文件上传失败", e);
            throw new RuntimeException("MinIO文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 确保存储桶存在并设置为公开访问
     */
    private void ensureBucketExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build());
            
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());
                log.info("创建存储桶: {}", minioConfig.getBucketName());
            }
            
            // 设置存储桶策略为公开读取
            try {
                String policy = String.format(
                    "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}",
                    minioConfig.getBucketName()
                );
                
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .config(policy)
                    .build());
                log.info("设置存储桶 {} 为公开访问", minioConfig.getBucketName());
            } catch (Exception e) {
                log.warn("设置存储桶策略失败: {}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("检查/创建存储桶失败", e);
            throw new RuntimeException("存储桶操作失败: " + e.getMessage(), e);
        }
    }
} 