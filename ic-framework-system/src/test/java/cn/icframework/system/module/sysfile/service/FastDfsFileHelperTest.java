package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.config.FastDfsConfig;
import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.service.SysFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * FastDFS文件帮助类测试
 * @author ic
 * @since 2024/12/19
 */
@ExtendWith(MockitoExtension.class)
class FastDfsFileHelperTest {

    @Mock
    private SysFileService sysFileService;

    @Mock
    private FastDfsConfig fastDfsConfig;

    @Mock
    private com.github.tobato.fastdfs.service.FastFileStorageClient fastFileStorageClient;

    @InjectMocks
    private FastDfsFileHelper fastDfsFileHelper;

    @BeforeEach
    void setUp() {
        // 设置FastDFS配置
        when(fastDfsConfig.getServerUrl()).thenReturn("http://localhost:8080");
    }

    @Test
    void testRegisterUploadSlice() {
        // 准备测试数据
        FileUseType useType = FileUseType.AVATAR;
        String fileName = "test.jpg";
        int totalParts = 3;
        Long userId = 1L;

        // 执行测试
        String uploadId = fastDfsFileHelper.registerUploadSlice(useType, fileName, totalParts, userId);

        // 验证结果
        assertNotNull(uploadId);
        assertTrue(uploadId.startsWith("fdfs_"));
        assertEquals(36 + 5, uploadId.length()); // UUID长度 + "fdfs_"前缀长度
    }

    @Test
    void testUploadSingle() throws Exception {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test content".getBytes()
        );
        FileUseType useType = FileUseType.AVATAR;
        Long userId = 1L;

        // Mock FastDFS客户端
        com.github.tobato.fastdfs.domain.fdfs.StorePath storePath = 
                new com.github.tobato.fastdfs.domain.fdfs.StorePath("group1", "M00/00/00/test.jpg");
        when(fastFileStorageClient.uploadFile(any(), anyLong(), anyString(), any()))
                .thenReturn(storePath);

        // Mock SysFileService
        when(sysFileService.insert(any(SysFile.class))).thenReturn(1);

        // 执行测试
        SysFile result = fastDfsFileHelper.uploadSingle(file, useType, userId);

        // 验证结果
        assertNotNull(result);
        assertEquals("test.jpg", result.getName());
        assertEquals(FileUseType.AVATAR.text() + "/", result.getOssObjectName().substring(0, 7));
        assertEquals("fastdfs", result.getBucketName());
        assertEquals(11L, result.getSize());
        assertEquals(userId, result.getUserId());

        // 验证方法调用
        verify(sysFileService, times(1)).insert(any(SysFile.class));
        verify(fastFileStorageClient, times(1)).uploadFile(any(), anyLong(), anyString(), any());
    }

    @Test
    void testUploadSlice() {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test content".getBytes()
        );
        String uploadId = "fdfs_test_upload_id";
        int partNumber = 1;

        // 执行测试 - 由于分片上传需要缓存支持，这里主要测试方法不会抛出异常
        assertDoesNotThrow(() -> {
            SysFile result = fastDfsFileHelper.uploadSlice(file, uploadId, partNumber);
            // 由于缓存中没有对应的上传信息，应该返回null
            assertNull(result);
        });
    }

    @Test
    void testAbortUploadSlice() {
        // 准备测试数据
        String uploadId = "fdfs_test_upload_id";

        // 执行测试 - 验证方法不会抛出异常
        assertDoesNotThrow(() -> {
            fastDfsFileHelper.abortUploadSlice(uploadId);
        });
    }

    @Test
    void testUploadSingleWithNullFile() {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new byte[0]
        );
        FileUseType useType = FileUseType.AVATAR;
        Long userId = 1L;

        // Mock FastDFS客户端抛出异常
        when(fastFileStorageClient.uploadFile(any(), anyLong(), anyString(), any()))
                .thenThrow(new RuntimeException("Upload failed"));

        // 执行测试并验证异常
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fastDfsFileHelper.uploadSingle(file, useType, userId);
        });

        assertTrue(exception.getMessage().contains("FastDFS单文件上传失败"));
    }

    @Test
    void testUploadSingleWithLargeFile() throws Exception {
        // 准备测试数据 - 大文件
        byte[] largeContent = new byte[1024 * 1024]; // 1MB
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "large.jpg",
                "image/jpeg",
                largeContent
        );
        FileUseType useType = FileUseType.HOTEL_PIC;
        Long userId = 1L;

        // Mock FastDFS客户端
        com.github.tobato.fastdfs.domain.fdfs.StorePath storePath = 
                new com.github.tobato.fastdfs.domain.fdfs.StorePath("group1", "M00/00/00/large.jpg");
        when(fastFileStorageClient.uploadFile(any(), anyLong(), anyString(), any()))
                .thenReturn(storePath);

        // Mock SysFileService
        when(sysFileService.insert(any(SysFile.class))).thenReturn(1);

        // 执行测试
        SysFile result = fastDfsFileHelper.uploadSingle(file, useType, userId);

        // 验证结果
        assertNotNull(result);
        assertEquals("large.jpg", result.getName());
        assertEquals(1024 * 1024L, result.getSize());
        assertEquals(FileUseType.HOTEL_PIC.text() + "/", result.getOssObjectName().substring(0, 9));
    }
} 