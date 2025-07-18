package cn.icframework.system.module.sysfile.service;

import cn.icframework.cache.utils.CacheUtils;
import cn.icframework.system.config.OssConfig;
import cn.icframework.system.enums.FileType;
import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.enums.PrivateFileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import com.aliyun.oss.model.AbortMultipartUploadRequest;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author hzl
 * @since 2024/8/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OssFileHelper {
    private final SysFileService sysFileService;
    private final OssConfig ossConfig;

    /**
     * 获取分配上传uploadId
     *
     * @param fileName 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
     */
    public String registerUploadSlice(FileUseType useType, String fileName, int totalParts, Long userId) {
        String type = "";
        if (fileName != null && fileName.contains(".")) {
            type = fileName.substring(fileName.indexOf("."));
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String objectName = useType.text() + "/" + uuid + type;
        String bucketName = ossConfig.getBucketName();
        String uploadId = ossConfig.getOss().initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, objectName)).getUploadId();
        // 缓存这个上传id
        CacheUtils.set(uploadId, new UploadCache(bucketName, objectName, fileName, useType, totalParts, userId));
        return uploadId;
    }

    /**
     * 上传
     *
     * @param uploadId
     */
    public SysFile upload(MultipartFile file, String uploadId, int partNumber) throws IOException {
        UploadCache uploadCache = (UploadCache) CacheUtils.get(uploadId);
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(uploadCache.getBucketName());
        uploadPartRequest.setKey(uploadCache.getObjectName());
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setInputStream(file.getInputStream());
        // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
        uploadPartRequest.setPartSize(file.getSize());
        // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
        uploadPartRequest.setPartNumber(partNumber);
        // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
        UploadPartResult uploadPartResult = ossConfig.getOss().uploadPart(uploadPartRequest);
        // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
        uploadCache.getPartETags().add(uploadPartResult.getPartETag());
        // TODO 分布式需要加锁
        if (uploadCache.getTotalParts() == uploadCache.getPartETags().size()) {
            // 都上传完了，做合并
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(uploadCache.getBucketName(), uploadCache.getObjectName(), uploadId, uploadCache.getPartETags());
            // 完成分片上传。
            ossConfig.getOss().completeMultipartUpload(completeMultipartUploadRequest);

            if (!PrivateFileUseType.PRIVATE_FILE_USE_TYPE.contains(uploadCache.useType)) {
                try {
                    // 头像放到公开链接
                    ossConfig.getOss().setObjectAcl(uploadCache.getBucketName(), uploadCache.getObjectName(), CannedAccessControlList.PublicRead);
                } catch (Exception e) {
                    log.error("设置公共读取失败", e);
                }
            }

            String url = "https://" + uploadCache.getBucketName() + "." + ossConfig.getEndpoint();
            SysFile sysFile = new SysFile();
            sysFile.setOssObjectName(uploadCache.getObjectName());
            sysFile.setName(uploadCache.getFileName());
            sysFile.setRefCount(0);
            sysFile.setBucketUrl(url);
            sysFile.setSize(file.getSize());
            sysFile.setType(FileType.ALIYUN);
            sysFile.setUserId(uploadCache.getUserId());
            sysFileService.insert(sysFile);
            return sysFile;
        }
        return null;
    }


    /**
     * 取消上传
     *
     * @param uploadId
     */
    public void abortUploadSlice(String uploadId) {
        UploadCache uploadCache = (UploadCache) CacheUtils.get(uploadId);
        // 取消分片上传。
        AbortMultipartUploadRequest abortMultipartUploadRequest =
                new AbortMultipartUploadRequest(uploadCache.getBucketName(), uploadCache.getObjectName(), uploadId);
        ossConfig.getOss().abortMultipartUpload(abortMultipartUploadRequest);
    }

    @Transactional
    public SysFile uploadSingle(MultipartFile file, FileUseType useType, Long userId) throws IOException {
        //获取上传文件输入流
        InputStream inputStream = file.getInputStream();
        String name = file.getOriginalFilename();
        String type = "";
        if (name != null && name.contains(".")) {
            type = name.substring(name.indexOf("."));
        }
        String bucketName = ossConfig.getBucketName();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String objectName = useType.text() + "/" + uuid + type;
        ossConfig.getOss().putObject(bucketName, objectName, inputStream);

        if (!PrivateFileUseType.PRIVATE_FILE_USE_TYPE.contains(useType)) {
            try {
                // 头像放到公开链接
                ossConfig.getOss().setObjectAcl(bucketName, objectName, CannedAccessControlList.PublicRead);
            } catch (Exception e) {
                log.error("设置公共读取失败", e);
            }
        }

        String url = "https://" + bucketName + "." + ossConfig.getEndpoint();
        SysFile sysFile = new SysFile();
        sysFile.setBucketName(bucketName);
        sysFile.setOssObjectName(objectName);
        sysFile.setName(file.getOriginalFilename());
        sysFile.setRefCount(0);
        sysFile.setBucketUrl(url);
        sysFile.setSize(file.getSize());
        sysFile.setType(FileType.ALIYUN);
        sysFile.setUserId(userId);
        sysFileService.insert(sysFile);
        return sysFile;
    }


    @Getter
    @Setter
    public static class UploadCache {
        private Long userId;
        private String bucketName;
        private String objectName;
        private String fileName;
        private FileUseType useType;
        private int totalParts;
        private List<PartETag> partETags = new ArrayList<>();

        public UploadCache(String bucketName, String objectName, String fileName, FileUseType useType, int totalParts, Long userId) {
            this.useType = useType;
            this.bucketName = bucketName;
            this.fileName = fileName;
            this.objectName = objectName;
            this.totalParts = totalParts;
            this.userId = userId;
        }
    }
}
