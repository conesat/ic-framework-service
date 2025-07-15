package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

public enum ChatType implements IEnum {
    GROUP(1, "群组"),
    USER(2, "用户");

    private final int code;
    private final String text;

    ChatType(int code, String text) {
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

    public static ChatType instanceOf(Integer code) {
        if (code == null) {
            return USER;
        }
        for (ChatType menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return USER;
    }
}
