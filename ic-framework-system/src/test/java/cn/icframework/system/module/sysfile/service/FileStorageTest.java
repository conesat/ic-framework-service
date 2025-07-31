package cn.icframework.system.module.sysfile.service;

import cn.icframework.system.config.FileStorageConfig;
import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件存储功能测试
 * @author ic
 * @since 2024/12/19
 */
class FileStorageTest {

    @Test
    void testFileStorageConfig() {
        // 测试文件存储配置
        FileStorageConfig config = new FileStorageConfig();
        
        // 测试默认值
        assertEquals("oss", config.getType());
        assertTrue(config.isEnableSliceUpload());
        assertEquals(5242880L, config.getSliceSize());
        
        // 测试设置值
        config.setType("fastdfs");
        config.setEnableSliceUpload(false);
        config.setSliceSize(10485760L);
        
        assertEquals("fastdfs", config.getType());
        assertFalse(config.isEnableSliceUpload());
        assertEquals(10485760L, config.getSliceSize());
    }

    @Test
    void testFileStorageStrategy() {
        // 测试文件存储策略
        FileStorageConfig config = new FileStorageConfig();
        config.setType("oss");
        
        // 创建策略实例
        FileStorageStrategy strategy = new FileStorageStrategy(
                config,
                new OssFileHelper(null, null),
                new FastDfsFileHelper(null, null, null)
        );
        
        // 验证策略选择
        IFileHelper ossHelper = strategy.getFileHelper();
        assertNotNull(ossHelper);
        assertTrue(ossHelper instanceof OssFileHelper);
        
        // 测试FastDFS策略
        config.setType("fastdfs");
        FileStorageStrategy fastDfsStrategy = new FileStorageStrategy(
                config,
                new OssFileHelper(null, null),
                new FastDfsFileHelper(null, null, null)
        );
        
        IFileHelper fastDfsHelper = fastDfsStrategy.getFileHelper();
        assertNotNull(fastDfsHelper);
        assertTrue(fastDfsHelper instanceof FastDfsFileHelper);
    }

    @Test
    void testFileStorageStrategyWithUnknownType() {
        // 测试未知存储类型
        FileStorageConfig config = new FileStorageConfig();
        config.setType("unknown");
        
        FileStorageStrategy strategy = new FileStorageStrategy(
                config,
                new OssFileHelper(null, null),
                new FastDfsFileHelper(null, null, null)
        );
        
        // 应该返回默认的OSS实现
        IFileHelper helper = strategy.getFileHelper();
        assertNotNull(helper);
        assertTrue(helper instanceof OssFileHelper);
    }

    @Test
    void testFileStorageStrategyCaseInsensitive() {
        // 测试大小写不敏感
        FileStorageConfig config = new FileStorageConfig();
        config.setType("FASTDFS");
        
        FileStorageStrategy strategy = new FileStorageStrategy(
                config,
                new OssFileHelper(null, null),
                new FastDfsFileHelper(null, null, null)
        );
        
        IFileHelper helper = strategy.getFileHelper();
        assertNotNull(helper);
        assertTrue(helper instanceof FastDfsFileHelper);
    }

    @Test
    void testMockMultipartFile() {
        // 测试MockMultipartFile
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test content".getBytes()
        );
        
        assertEquals("test.jpg", file.getOriginalFilename());
        assertEquals("image/jpeg", file.getContentType());
        assertEquals(12L, file.getSize());
        assertFalse(file.isEmpty());
    }

    @Test
    void testFileUseType() {
        // 测试文件用途类型
        assertEquals("avatar", FileUseType.AVATAR.text());
        assertEquals("public", FileUseType.PUBLIC.text());
        assertEquals("hotelPic", FileUseType.HOTEL_PIC.text());
        assertEquals("product", FileUseType.PRODUCT_PIC.text());
    }

    @Test
    void testSysFileCreation() {
        // 测试SysFile对象创建
        SysFile sysFile = new SysFile();
        sysFile.setId(1L);
        sysFile.setName("test.jpg");
        sysFile.setSize(1024L);
        sysFile.setUserId(1L);
        
        assertEquals(1L, sysFile.getId());
        assertEquals("test.jpg", sysFile.getName());
        assertEquals(1024L, sysFile.getSize());
        assertEquals(1L, sysFile.getUserId());
    }
} 