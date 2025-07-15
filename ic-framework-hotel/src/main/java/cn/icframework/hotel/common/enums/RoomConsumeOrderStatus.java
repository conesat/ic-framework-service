package cn.icframework.hotel.common.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * 消费状态
 */
public enum RoomConsumeOrderStatus implements IEnum {
    UN_PAY(0, "待支付"),
    PAYED(1, "已支付");

    private final int code;
    private final String text;

    RoomConsumeOrderStatus(int code, String text) {
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


    public static RoomConsumeOrderStatus instanceOf(Integer code) {
        if (code == null) {
            return UN_PAY;
        }
        for (RoomConsumeOrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UN_PAY;
    }
}
