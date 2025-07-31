package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * @author hzl
 * @since 2023/6/21
 */
public enum FileType implements IEnum {
    UNKNOWN(-1, "未知"),
    ALIYUN(1, "阿里云"),
    FASTDFS(2, "FastDFS");

    private final int code;
    private final String text;

    FileType(int code, String text) {
        this.text = text;
        this.code = code;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String text() {
        return text;
    }

    public static FileType instanceOf(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (FileType fileType : values()) {
            if (fileType.code == code) {
                return fileType;
            }
        }
        return UNKNOWN;
    }
}
