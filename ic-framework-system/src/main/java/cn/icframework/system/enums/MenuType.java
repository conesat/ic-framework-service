package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

public enum MenuType implements IEnum {
    UNKNOWN(-1, "未知"),
    LAYOUT(1, "路由"),
    BLANK(2, "新窗口"),
    IFRAME(3, "弹窗");

    private final int code;
    private final String text;

    MenuType(int code, String text) {
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

    public static MenuType instanceOf(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (MenuType menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return UNKNOWN;
    }
}
