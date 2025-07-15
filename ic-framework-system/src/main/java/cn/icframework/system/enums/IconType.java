package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

public enum IconType implements IEnum {
    UNKNOWN(-1, "未知"),
    IC(1, "ic"),
    TDesign(2, "TDesign");

    private final int code;
    private final String text;

    IconType(int code, String text) {
        this.text = text;
        this.code = code;
    }
    IconType() {
        this.text = null;
        this.code = 0;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String text() {
        return text;
    }

    public static IconType instanceOf(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (IconType menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return UNKNOWN;
    }
}
