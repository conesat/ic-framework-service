package cn.icframework.hotel.common.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * 通用状态
 * @author hzl
 * @date 2023/6/21
 */
public enum ServerTargetEnum implements IEnum {
    CLEAR(1, "清洁"),

    SALE(2, "送货");

    private final int code;
    private final String text;

    ServerTargetEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String text() {
        return text;
    }


    public static ServerTargetEnum instanceOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (ServerTargetEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
