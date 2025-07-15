package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

public enum MenuPlatformType implements IEnum {
    UNKNOWN(-1, "未知"),
    SYS(1, "内部平台");

    private final int code;
    private final String text;

    MenuPlatformType(int code, String text) {
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

    public static MenuPlatformType instanceOf(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (MenuPlatformType menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return UNKNOWN;
    }
}
