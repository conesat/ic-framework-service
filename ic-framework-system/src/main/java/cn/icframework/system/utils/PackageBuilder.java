package cn.icframework.system.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 简化的打包工具类
 * 用于构建更新包，包含 Maven 编译、版本文件生成、ZIP 打包、AES 加密等功能
 *
 * @author ic
 * @since 2024/12/19
 */
public class PackageBuilder {

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "ic-framework-202"; // 16 字节密钥
    private static final String PACKAGE_SUFFIX = ".zip";
    private static final String IC_EXTENSION = ".ic";

    /**
     * 从 pom.xml 文件读取版本号
     *
     * @param projectPath 项目路径
     * @return 版本号，如果读取失败返回 null
     */
    public static String readVersionFromPom(String projectPath) {
        try {
            Path pomPath = Paths.get(projectPath, "pom.xml");
            if (!Files.exists(pomPath)) {
                System.err.println("未找到 pom.xml 文件: " + pomPath);
                return null;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(pomPath.toFile());
            document.getDocumentElement().normalize();

            // 首先尝试读取当前项目的版本
            NodeList versionNodes = document.getElementsByTagName("version");
            if (versionNodes.getLength() > 0) {
                String version = versionNodes.item(0).getTextContent().trim();
                if (!version.isEmpty() && !version.startsWith("${")) {
                    System.out.println("从 pom.xml 读取到版本号: " + version);
                    return version;
                }
            }

            // 如果当前项目没有版本，尝试从父项目读取
            NodeList parentNodes = document.getElementsByTagName("parent");
            if (parentNodes.getLength() > 0) {
                Element parent = (Element) parentNodes.item(0);
                NodeList parentVersionNodes = parent.getElementsByTagName("version");
                if (parentVersionNodes.getLength() > 0) {
                    String parentVersion = parentVersionNodes.item(0).getTextContent().trim();
                    if (!parentVersion.isEmpty() && !parentVersion.startsWith("${")) {
                        System.out.println("从父项目 pom.xml 读取到版本号: " + parentVersion);
                        return parentVersion;
                    }
                }
            }

            System.err.println("无法从 pom.xml 读取版本号");
            return null;

        } catch (Exception e) {
            System.err.println("读取 pom.xml 版本号失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 构建更新包（自动读取版本号）
     *
     * @param projectPath 项目路径
     * @param outputPath  输出路径（可选，默认为 target 目录）
     * @return 加密后的包文件路径
     */
    public static String buildUpdatePackage(String projectPath, String outputPath) {
        String version = readVersionFromPom(projectPath);
        if (version == null) {
            throw new RuntimeException("无法从 pom.xml 读取版本号");
        }
        return buildUpdatePackage(projectPath, outputPath, version);
    }

    /**
     * 构建更新包
     *
     * @param projectPath 项目路径
     * @param outputPath  输出路径（可选，默认为 target 目录）
     * @param version     版本号
     * @return 加密后的包文件路径
     */
    public static String buildUpdatePackage(String projectPath, String outputPath, String version) {
        try {
            System.out.println("开始构建更新包，项目路径: " + projectPath + ", 输出路径: " + outputPath + ", 版本: " + version);

            // 如果输出路径为空，使用 target 目录
            if (outputPath == null || outputPath.trim().isEmpty()) {
                outputPath = Paths.get(projectPath, "target").toString();
            }

            // 编译 Maven 项目
            String jarPath = compileMavenProject(projectPath);
            if (jarPath == null) {
                throw new RuntimeException("Maven 编译失败");
            }

            // 生成版本文件
            String versionFilePath = generateVersionFile(outputPath, version);

            // 创建 ZIP 包
            String zipPath = createZipPackage(jarPath, versionFilePath, outputPath, version);

            // 加密 ZIP 包
            String encryptedPath = encryptZipPackage(zipPath);

            System.out.println("更新包构建完成: " + encryptedPath);
            return encryptedPath;

        } catch (Exception e) {
            System.err.println("构建更新包失败: " + e.getMessage());
            throw new RuntimeException("构建更新包失败: " + e.getMessage(), e);
        }
    }

    /**
     * 编译 Maven 项目
     *
     * @param projectPath 项目路径
     * @return JAR 文件路径
     */
    private static String compileMavenProject(String projectPath) {
        try {
            System.out.println("开始编译 Maven 项目: " + projectPath);

            // 创建 Maven 编译进程
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(projectPath));
            processBuilder.command("mvn", "clean", "package", "-DskipTests");

            // 重定向错误流到标准输出
            processBuilder.redirectErrorStream(true);

            // 启动进程
            Process process = processBuilder.start();

            // 读取输出
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 只输出关键信息
                    if (line.contains("BUILD") || line.contains("SUCCESS") || line.contains("FAILURE")) {
                        System.out.println("Maven: " + line);
                    }
                }
            }

            // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Maven 编译失败，退出码: " + exitCode);
                return null;
            }

            // 查找生成的 JAR 文件
            String jarPath = findJarFile(projectPath);
            if (jarPath != null) {
                System.out.println("Maven 编译成功，JAR 文件: " + jarPath);
                return jarPath;
            } else {
                System.err.println("未找到生成的 JAR 文件");
                return null;
            }

        } catch (Exception e) {
            System.err.println("Maven 编译异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 查找 JAR 文件
     *
     * @param projectPath 项目路径
     * @return JAR 文件路径
     */
    private static String findJarFile(String projectPath) {
        try {
            // 首先尝试查找当前目录下的 target 文件夹
            Path targetPath = Paths.get(projectPath, "target");
            if (Files.exists(targetPath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetPath, "*.jar")) {
                    for (Path path : stream) {
                        if (Files.isRegularFile(path)) {
                            System.out.println("找到 JAR 文件: " + path);
                            return path.toString();
                        }
                    }
                }
            }

            // 如果没找到，尝试查找子模块的 target 文件夹
            try (DirectoryStream<Path> modules = Files.newDirectoryStream(Paths.get(projectPath))) {
                for (Path module : modules) {
                    if (Files.isDirectory(module)) {
                        Path moduleTargetPath = module.resolve("target");
                        if (Files.exists(moduleTargetPath)) {
                            try (DirectoryStream<Path> stream = Files.newDirectoryStream(moduleTargetPath, "*.jar")) {
                                for (Path path : stream) {
                                    if (Files.isRegularFile(path)) {
                                        System.out.println("找到模块 JAR 文件: " + path);
                                        return path.toString();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            System.err.println("未找到任何 JAR 文件");
            return null;
        } catch (Exception e) {
            System.err.println("查找 JAR 文件失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 生成版本文件
     *
     * @param outputPath 输出路径
     * @param version    版本号
     * @return 版本文件路径
     */
    private static String generateVersionFile(String outputPath, String version) {
        try {
            // 创建输出目录
            Files.createDirectories(Paths.get(outputPath));

            // 生成版本信息
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            String versionContent = String.format("version=%s\ntimestamp=%s",
                    version, timestamp);

            // 写入版本文件
            String versionFileName = "version" + ".txt";
            Path versionFilePath = Paths.get(outputPath, versionFileName);
            Files.write(versionFilePath, versionContent.getBytes());

            System.out.println("版本文件生成完成: " + versionFilePath);
            return versionFilePath.toString();

        } catch (Exception e) {
            System.err.println("生成版本文件失败: " + e.getMessage());
            throw new RuntimeException("生成版本文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建 ZIP 包
     *
     * @param jarPath         JAR 文件路径
     * @param versionFilePath 版本文件路径
     * @param outputPath      输出路径
     * @param version         版本号
     * @return ZIP 文件路径
     */
    private static String createZipPackage(String jarPath, String versionFilePath, String outputPath, String version) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            String zipFileName = "update_" + version + "_" + timestamp + PACKAGE_SUFFIX;
            Path zipPath = Paths.get(outputPath, zipFileName);

            System.out.println("开始创建 ZIP 包: " + zipPath);

            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
                // 添加 JAR 文件
                addFileToZip(zipOut, new File(jarPath), "app.jar");

                // 添加版本文件
                addFileToZip(zipOut, new File(versionFilePath), "version.txt");

                // 添加 resources 目录（如果存在）
                addResourcesToZip(zipOut, Paths.get(jarPath).getParent().getParent().resolve("src/main/resources"));
            }

            System.out.println("ZIP 包创建完成: " + zipPath);
            return zipPath.toString();

        } catch (Exception e) {
            System.err.println("创建 ZIP 包失败: " + e.getMessage());
            throw new RuntimeException("创建 ZIP 包失败: " + e.getMessage(), e);
        }
    }

    /**
     * 添加文件到 ZIP
     *
     * @param zipOut    ZIP 输出流
     * @param file      文件
     * @param entryName ZIP 条目名称
     */
    private static void addFileToZip(ZipOutputStream zipOut, File file, String entryName) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }

    /**
     * 添加 resources 目录到 ZIP
     *
     * @param zipOut        ZIP 输出流
     * @param resourcesPath resources 路径
     */
    private static void addResourcesToZip(ZipOutputStream zipOut, Path resourcesPath) {
        if (!Files.exists(resourcesPath)) {
            System.out.println("Resources 目录不存在，跳过: " + resourcesPath);
            return;
        }

        try {
            Files.walkFileTree(resourcesPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!Files.isDirectory(file)) {
                        String fileName = file.getFileName().toString();
                        // 排除 *-dev.* 文件
                        if (fileName.matches(".*-dev\\..*")) {
                            System.out.println("跳过开发环境文件: " + fileName);
                            return FileVisitResult.CONTINUE;
                        }
                        
                        String relativePath = "resources/" + resourcesPath.relativize(file).toString();
                        addFileToZip(zipOut, file.toFile(), relativePath);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (!dir.equals(resourcesPath)) {
                        String dirName = dir.getFileName().toString();
                        // 排除包含 -dev 的目录
                        if (dirName.contains("-dev")) {
                            System.out.println("跳过开发环境目录: " + dirName);
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                        
                        String relativePath = "resources/" + resourcesPath.relativize(dir).toString() + "/";
                        ZipEntry zipEntry = new ZipEntry(relativePath);
                        zipOut.putNextEntry(zipEntry);
                        zipOut.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("添加 resources 到 ZIP 失败: " + e.getMessage());
        }
    }

    /**
     * 加密 ZIP 包
     *
     * @param zipPath ZIP 文件路径
     * @return 加密后的文件路径
     */
    private static String encryptZipPackage(String zipPath) {
        try {
            Path zipFilePath = Paths.get(zipPath);
            String encryptedFileName = zipFilePath.getFileName().toString().replace(PACKAGE_SUFFIX, IC_EXTENSION);
            Path encryptedPath = zipFilePath.resolveSibling(encryptedFileName);

            System.out.println("开始加密 ZIP 包: " + encryptedPath);

            // 读取 ZIP 文件
            byte[] zipData = Files.readAllBytes(zipFilePath);

            // 加密数据
            byte[] encryptedData = encryptData(zipData);

            // 写入加密文件
            Files.write(encryptedPath, encryptedData);

            // 删除原始 ZIP 文件
            Files.deleteIfExists(zipFilePath);

            System.out.println("ZIP 包加密完成: " + encryptedPath);
            return encryptedPath.toString();

        } catch (Exception e) {
            System.err.println("加密 ZIP 包失败: " + e.getMessage());
            throw new RuntimeException("加密 ZIP 包失败: " + e.getMessage(), e);
        }
    }

    /**
     * 加密数据
     *
     * @param data 原始数据
     * @return 加密后的数据
     */
    private static byte[] encryptData(byte[] data) throws Exception {
        // 创建密钥
        SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

        // 创建加密器
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // 加密数据
        return cipher.doFinal(data);
    }

    /**
     * 生成文件 MD5 校验和
     *
     * @param filePath 文件路径
     * @return MD5 校验和
     */
    public static String generateMD5(String filePath) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(Files.readAllBytes(Paths.get(filePath)));

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            System.err.println("生成 MD5 失败: " + e.getMessage());
            return null;
        }
    }
} 