package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

public enum NoticeType implements IEnum {
    UNKNOWN(-1, "未知"),
    NOTICE(1, "通知"),
    ANNOUNCEMENT(2, "公告");

    private final int code;
    private final String text;

    NoticeType(int code, String text) {
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

    public static NoticeType instanceOf(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (NoticeType menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return UNKNOWN;
    }
}
