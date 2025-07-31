package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.config.FileStorageConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

/**
 * 文件存储策略测试
 * @author ic
 * @since 2024/12/19
 */
@ExtendWith(MockitoExtension.class)
class FileStorageStrategyTest {

    @Mock
    private FileStorageConfig fileStorageConfig;

    @Mock
    private OssFileHelper ossFileHelper;

    @Mock
    private FastDfsFileHelper fastDfsFileHelper;

    @InjectMocks
    private FileStorageStrategy fileStorageStrategy;

    @BeforeEach
    void setUp() {
        // 设置默认配置 - 使用lenient避免不必要的stubbing警告
        lenient().when(fileStorageConfig.isEnableSliceUpload()).thenReturn(true);
        lenient().when(fileStorageConfig.getSliceSize()).thenReturn(5242880L);
    }

    @Test
    void testGetFileHelperWithOss() {
        // 准备测试数据
        when(fileStorageConfig.getType()).thenReturn("oss");

        // 执行测试
        IFileHelper result = fileStorageStrategy.getFileHelper();

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof OssFileHelper);
    }

    @Test
    void testGetFileHelperWithFastdfs() {
        // 准备测试数据
        when(fileStorageConfig.getType()).thenReturn("fastdfs");

        // 执行测试
        IFileHelper result = fileStorageStrategy.getFileHelper();

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof FastDfsFileHelper);
    }

    @Test
    void testGetFileHelperWithUpperCase() {
        // 准备测试数据
        when(fileStorageConfig.getType()).thenReturn("FASTDFS");

        // 执行测试
        IFileHelper result = fileStorageStrategy.getFileHelper();

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof FastDfsFileHelper);
    }

    @Test
    void testGetFileHelperWithUnknownType() {
        // 准备测试数据
        when(fileStorageConfig.getType()).thenReturn("unknown");

        // 执行测试
        IFileHelper result = fileStorageStrategy.getFileHelper();

        // 验证结果 - 未知类型应该返回默认的OSS实现
        assertNotNull(result);
        assertTrue(result instanceof OssFileHelper);
    }

    @Test
    void testGetFileHelperWithNullType() {
        // 准备测试数据
        when(fileStorageConfig.getType()).thenReturn(null);

        // 执行测试
        IFileHelper result = fileStorageStrategy.getFileHelper();

        // 验证结果 - null类型应该返回默认的OSS实现
        assertNotNull(result);
        assertTrue(result instanceof OssFileHelper);
    }

    @Test
    void testGetStorageType() {
        // 准备测试数据
        String expectedType = "fastdfs";
        when(fileStorageConfig.getType()).thenReturn(expectedType);

        // 执行测试
        String result = fileStorageStrategy.getStorageType();

        // 验证结果
        assertEquals(expectedType, result);
    }

    @Test
    void testIsEnableSliceUpload() {
        // 准备测试数据
        when(fileStorageConfig.isEnableSliceUpload()).thenReturn(true);

        // 执行测试
        boolean result = fileStorageStrategy.isEnableSliceUpload();

        // 验证结果
        assertTrue(result);
    }

    @Test
    void testIsEnableSliceUploadFalse() {
        // 准备测试数据
        when(fileStorageConfig.isEnableSliceUpload()).thenReturn(false);

        // 执行测试
        boolean result = fileStorageStrategy.isEnableSliceUpload();

        // 验证结果
        assertFalse(result);
    }

    @Test
    void testGetSliceSize() {
        // 准备测试数据
        long expectedSize = 10485760L; // 10MB
        when(fileStorageConfig.getSliceSize()).thenReturn(expectedSize);

        // 执行测试
        long result = fileStorageStrategy.getSliceSize();

        // 验证结果
        assertEquals(expectedSize, result);
    }

    @Test
    void testGetSliceSizeDefault() {
        // 准备测试数据
        long expectedSize = 5242880L; // 5MB
        when(fileStorageConfig.getSliceSize()).thenReturn(expectedSize);

        // 执行测试
        long result = fileStorageStrategy.getSliceSize();

        // 验证结果
        assertEquals(expectedSize, result);
    }
} 