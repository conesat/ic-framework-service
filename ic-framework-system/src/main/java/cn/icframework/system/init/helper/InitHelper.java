package cn.icframework.system.init.helper;

import cn.icframework.core.utils.FileUtils;
import cn.icframework.core.utils.MD5Util;
import cn.icframework.system.utils.InitMd5Utils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * 数据初始化工具
 */
@Slf4j
@Component
public class InitHelper {
    private String rootDic = "";

    @PostConstruct
    public void init() {
        String currentDir = System.getProperty("user.dir");
        rootDic = Objects.requireNonNull(getClass().getClassLoader().getResource("")).toString().replace("file:", "");
        if (rootDic.startsWith("jar")) {
            rootDic = currentDir + "/resources/";
        }
    }

    public void processDic(String path, String key, ContentCallback callback) throws IOException {
        File file = new File(rootDic + path);
        if (!file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        assert files != null;
        Set<String> currentMd5Keys = key == null ? null : new HashSet<>();
        for (File f : files) {
            String filePath = f.getPath().substring(rootDic.length() - 1);
            if (currentMd5Keys != null) {
                currentMd5Keys.add(toMd5Key(filePath));
            }
            processFile(filePath, key, callback);
        }
        if (key != null) {
            JSONObject md5Json = InitMd5Utils.getMd5Json(key);
            boolean change = false;
            for (String md5Key : new HashSet<>(md5Json.keySet())) {
                if (!currentMd5Keys.contains(md5Key)) {
                    md5Json.remove(md5Key);
                    change = true;
                }
            }
            if (change) {
                InitMd5Utils.saveMd5Json(key, md5Json);
            }
        }
    }

    public void processFile(String path, String key, ContentCallback callback) {
        File file = new File(rootDic + path);
        if (!file.exists()) {
            return;
        }
        boolean change = false;
        JSONObject md5Json = key == null ? null : InitMd5Utils.getMd5Json(key);
        String json = FileUtils.readFile(file); // 确保你有 readStream 方法支持 InputStream
        if (!StringUtils.hasLength(json)) {
            return;
        }
        if (key != null) {
            String md5Key = toMd5Key(path);
            String oldMd5 = md5Json.getString(md5Key);
            String md5 = MD5Util.encode(json);
            if (Objects.equals(oldMd5, md5)) {
                return;
            }
            change = true;
            md5Json.put(md5Key, md5);
        }
        callback.onContentReceived(json);
        if (change) {
            InitMd5Utils.saveMd5Json(key, md5Json);
        }
    }

    private String toMd5Key(String path) {
        String md5Key = path.replace('\\', '_').replace('/', '_');
        if (md5Key.startsWith("_")) {
            md5Key = md5Key.substring(1);
        }
        return md5Key;
    }
}
