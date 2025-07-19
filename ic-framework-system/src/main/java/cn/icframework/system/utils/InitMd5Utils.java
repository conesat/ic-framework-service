package cn.icframework.system.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 初始化md5工具
 */
public class InitMd5Utils {
    static final String filePath;

    static {
        String root = Objects.requireNonNull(InitMd5Utils.class.getClassLoader().getResource("")).getPath();
        String relativePath = root.substring(System.getProperty("user.dir").length() + 1);
        String[] pathComponents = relativePath.replace("\\", "/").split("/");
        filePath = Paths.get(System.getProperty("user.dir"), "md5", pathComponents[0]) + File.separator;

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    /**
     * 获取md5 json
     *
     * @param key md5 key
     * @return json
     */
    public static JSONObject getMd5Json(String key) {
        JSONObject jsonObject;
        File file = new File(filePath + key);
        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                jsonObject = JSONObject.parseObject(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    /**
     * 获取md5 string
     *
     * @param key md5 key
     * @return string
     */
    public static String getMd5String(String key) {
        File file = new File(filePath + key);
        if (file.exists()) {
            try {
                return new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "";
        }
    }

    /**
     * 保存md5 string
     *
     * @param key md5 key
     * @param md5 md5 string
     */
    public static void saveMd5String(String key, String md5) {
        try (FileWriter writer = new FileWriter(filePath + key)) {
            writer.write(md5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveMd5Json(String key, JSONObject md5Json) {
        try (FileWriter writer = new FileWriter(filePath + key)) {
            writer.write(md5Json.toJSONString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
