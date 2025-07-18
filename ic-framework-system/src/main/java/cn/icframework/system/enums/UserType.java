package cn.icframework.system.enums;

/**
 * 用户类型枚举
 * @author hzl
 * @since 2023/6/21
 */
public enum UserType {
    /**
     * 未知
     */
    UNKNOWN("UNKNOWN", "未知"),
    /**
     * 系统用户
     */
    SYSTEM_USER(cn.icframework.system.consts.UserType.SYSTEM_USER, "系统用户"),
    /**
     * 外部用户
     */
    CUSTOM_USER(cn.icframework.system.consts.UserType.CUSTOM_USER, "外部用户");

    private final String code;
    private final String text;

    UserType(String  code, String text) {
        this.text = text;
        this.code = code;
    }

    public String code() {
        return code;
    }

    public String text() {
        return text;
    }

    public static UserType instanceOf(String code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (UserType fileType : values()) {
            if (fileType.code.equals(code)) {
                return fileType;
            }
        }
        return UNKNOWN;
    }
}
