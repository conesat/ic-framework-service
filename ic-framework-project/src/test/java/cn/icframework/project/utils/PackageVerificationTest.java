package cn.icframework.project.utils;

import cn.icframework.system.utils.PackageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Properties;

/**
 * 打包验证测试用例
 * 用于校验 MD5、解密和解压 ZIP 包
 *
 * @author ic
 * @since 2024/12/19
 */
public class PackageVerificationTest {

    private static final String IC_EXTENSION = ".ic";
    private static final String DECRYPTED_EXTENSION = ".decrypted";
    private String projectPath;
    private String version;
    private String outputPath;
    private String packagePath;
    private String md5;

    @BeforeEach
    public void setUp() {
        projectPath = System.getProperty("user.dir");
        outputPath = projectPath + "/target";
        // 从 pom.xml 读取版本号
        version = PackageBuilder.readVersionFromPom(projectPath);
        if (version == null) {
            throw new RuntimeException("无法从 pom.xml 读取版本号");
        }
    }

    @Test
    public void testBuildAndVerifyPackage() {
        try {
            System.out.println("=== 开始测试打包和验证功能 ===");

            // 第一步：构建更新包
            System.out.println("📦 步骤1: 构建更新包");
            packagePath = PackageBuilder.buildUpdatePackage(projectPath, outputPath, version);
            System.out.println("✅ 更新包构建完成: " + packagePath);

            // 第二步：生成 MD5 校验和
            System.out.println("🔐 步骤2: 生成 MD5 校验和");
            md5 = PackageBuilder.generateMD5(packagePath);
            System.out.println("✅ MD5 校验和: " + md5);

            // 第三步：验证 MD5 校验和
            System.out.println("🔍 步骤3: 验证 MD5 校验和");
            verifyMD5(packagePath, md5);
            System.out.println("✅ MD5 校验和验证通过");

            // 第四步：解密 ZIP 包
            System.out.println("🔓 步骤4: 解密 ZIP 包");
            String decryptedPath = decryptPackage(packagePath);
            System.out.println("✅ ZIP 包解密完成: " + decryptedPath);

            // 第五步：解压 ZIP 包
            System.out.println("📂 步骤5: 解压 ZIP 包");
            String extractPath = extractZipPackage(decryptedPath);
            System.out.println("✅ ZIP 包解压完成: " + extractPath);

            // 第六步：验证解压后的文件
            System.out.println("✅ 步骤6: 验证解压后的文件");
            verifyExtractedFiles(extractPath);
            System.out.println("✅ 解压文件验证通过");

            System.out.println("🎉 所有测试通过！");
            System.out.println("📦 包文件: " + packagePath);
            System.out.println("🔐 MD5: " + md5);
            System.out.println("📂 解压目录: " + extractPath);

        } catch (Exception e) {
            System.err.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("打包验证测试失败", e);
        }
    }

    /**
     * 验证 MD5 校验和
     */
    private void verifyMD5(String filePath, String expectedMD5) throws Exception {
        String actualMD5 = PackageBuilder.generateMD5(filePath);
        if (!expectedMD5.equals(actualMD5)) {
            throw new RuntimeException("MD5 校验和验证失败！期望: " + expectedMD5 + ", 实际: " + actualMD5);
        }
        System.out.println("MD5 校验和验证成功: " + actualMD5);
    }

    /**
     * 解密 ZIP 包
     */
    private String decryptPackage(String encryptedPath) throws Exception {
        String decryptedPath = encryptedPath.replace(IC_EXTENSION, DECRYPTED_EXTENSION);

        try (FileInputStream fis = new FileInputStream(encryptedPath);
             FileOutputStream fos = new FileOutputStream(decryptedPath)) {

            byte[] key = "ic-framework-202".getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] decrypted = cipher.update(buffer, 0, bytesRead);
                if (decrypted != null) {
                    fos.write(decrypted);
                }
            }

            byte[] finalBlock = cipher.doFinal();
            if (finalBlock != null) {
                fos.write(finalBlock);
            }
        }

        return decryptedPath;
    }

    /**
     * 解压 ZIP 包
     */
    private String extractZipPackage(String zipPath) throws Exception {
        String extractedDirName = zipPath.substring(0, zipPath.lastIndexOf('.')) + "_extracted";
        Path extractedPath = Paths.get(extractedDirName);

        // 如果目录已存在，先删除
        if (Files.exists(extractedPath)) {
            deleteDirectory(extractedPath);
        }

        // 创建解压目录
        Files.createDirectories(extractedPath);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = extractedPath.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    // 确保父目录存在
                    Files.createDirectories(filePath.getParent());

                    // 写入文件
                    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }

        return extractedPath.toString();
    }

    /**
     * 验证解压后的文件
     */
    private void verifyExtractedFiles(String extractPath) throws Exception {
        Path extractDir = Paths.get(extractPath);

        // 检查是否存在 JAR 文件
        boolean hasJarFile = false;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(extractDir, "*.jar")) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    System.out.println("✅ 找到 JAR 文件: " + path.getFileName());
                    hasJarFile = true;
                    break;
                }
            }
        }

        if (!hasJarFile) {
            throw new RuntimeException("解压后未找到 JAR 文件");
        }

        // 检查是否存在版本文件
        boolean hasVersionFile = false;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(extractDir, "version.txt")) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    System.out.println("✅ 找到版本文件: " + path.getFileName());
                    verifyVersionFile(path);
                    hasVersionFile = true;
                    break;
                }
            }
        }

        if (!hasVersionFile) {
            throw new RuntimeException("解压后未找到版本文件");
        }

        // 检查是否存在资源目录
        Path resourcesDir = extractDir.resolve("resources");
        if (Files.exists(resourcesDir) && Files.isDirectory(resourcesDir)) {
            System.out.println("✅ 找到资源目录: " + resourcesDir.getFileName());
        } else {
            System.out.println("⚠️  未找到资源目录，这是正常的（如果没有额外资源）");
        }

        System.out.println("✅ 解压文件验证完成");
    }

    /**
     * 验证版本文件内容
     */
    private void verifyVersionFile(Path versionFilePath) throws Exception {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(versionFilePath.toFile())) {
            props.load(fis);
        }

        String versionValue = props.getProperty("version");
        String timestamp = props.getProperty("timestamp");

        if (versionValue == null || timestamp == null) {
            throw new RuntimeException("版本文件格式不正确");
        }

        System.out.println("📋 版本文件内容:");
        System.out.println("   版本号: " + versionValue);
        System.out.println("   时间: " + timestamp);

        if (!versionValue.equals(version)) {
            throw new RuntimeException("版本号不匹配！期望: " + version + ", 实际: " + versionValue);
        }

        System.out.println("✅ 版本文件验证通过");
    }

    /**
     * 清理测试文件
     */
    @Test
    public void testCleanup() {
        try {
            System.out.println("🧹 清理测试文件");

            // 查找并清理解密文件
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(outputPath), "*" + DECRYPTED_EXTENSION)) {
                for (Path path : stream) {
                    if (Files.isRegularFile(path)) {
                        Files.delete(path);
                        System.out.println("✅ 清理解密文件: " + path.getFileName());
                    }
                }
            }

            // 查找并清理解压目录
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(outputPath), "*_extracted")) {
                for (Path path : stream) {
                    if (Files.isDirectory(path)) {
                        deleteDirectory(path);
                        System.out.println("✅ 清理解压目录: " + path.getFileName());
                    }
                }
            }

            System.out.println("✅ 清理完成");

        } catch (Exception e) {
            System.err.println("⚠️  清理文件失败: " + e.getMessage());
        }
    }

    /**
     * 递归删除目录
     */
    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a)) // 先删除文件，再删除目录
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            System.err.println("删除文件失败: " + file + ", 错误: " + e.getMessage());
                        }
                    });
        }
    }
} 