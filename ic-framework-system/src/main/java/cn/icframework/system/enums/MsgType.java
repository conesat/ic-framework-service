package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

public enum MsgType implements IEnum {
    TEXT(1, "文字"),
    VIDEO(2, "视频"),
    VOICE(3, "音频"),
    PIC(4, "图片"),
    FILE(5, "文件"),
    SHARE(6, "分享");

    private final int code;
    private final String text;

    MsgType(int code, String text) {
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

    public static MsgType instanceOf(Integer code) {
        if (code == null) {
            return TEXT;
        }
        for (MsgType menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return TEXT;
    }
}
