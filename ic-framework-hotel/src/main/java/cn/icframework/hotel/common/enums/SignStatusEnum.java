package cn.icframework.hotel.common.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * 通用状态
 *
 * @author hzl
 * @date 2023/6/21
 */
public enum SignStatusEnum implements IEnum {
    NOT_CLOCKED_IN(0, "未打卡"),

    ATTENDED(1, "出勤"),
    MISSING(2, "缺卡"),

    ABSENT(3, "缺勤"),

    LATE(4, "迟到"),

    EARLY_LEAVE(5, "早退"),

    LATE_EARLY_LEAVE(6, "迟到早退");

    private final int code;
    private final String text;

    SignStatusEnum(int code, String text) {
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


    public static SignStatusEnum instanceOf(Integer code) {
        if (code == null) {
            return NOT_CLOCKED_IN;
        }
        for (SignStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return NOT_CLOCKED_IN;
    }
}
