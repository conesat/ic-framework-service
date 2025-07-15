package cn.icframework.hotel.common.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * 通用状态
 *
 * @author hzl
 * @date 2023/6/21
 */
public enum RoomStatusEnum implements IEnum {
    UNKNOWN(0, "未知"),
    AWAIT_CHECK_IN(1, "待入住"),
    CHECKED_IN(2, "已入住"),
    RESERVE(3, "预定"),
    CHECKED_IN_NEED_CLEANING(4, "已入住-待清洁"),
    CHECKED_OUT_PENDING_CLEANING(5, "待入住-待清洁"),
    CHECKED_IN_CLEANING(6, "已入住-清洁中"),
    CLEANING(7, "待入住-清洁中"),
    UNDER_MAINTENANCE(8, "检修中");

    private final int code;
    private final String text;

    RoomStatusEnum(int code, String text) {
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


    public static RoomStatusEnum instanceOf(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (RoomStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
