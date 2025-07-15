package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

public enum AdType implements IEnum {
    UNKNOWN(-1, "未知"),
    VIDEO(1, "视频"),
    LIVE(2, "直播"),
    PIC(3, "图片"),
    URL(4, "网页");

    private final int code;
    private final String text;

    AdType(int code, String text) {
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

    public static AdType instanceOf(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (AdType menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return UNKNOWN;
    }
}
