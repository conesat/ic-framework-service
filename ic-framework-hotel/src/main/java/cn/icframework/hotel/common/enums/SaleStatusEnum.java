package cn.icframework.hotel.common.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * 通用状态
 * @author hzl
 * @date 2023/6/21
 */
public enum SaleStatusEnum implements IEnum {
    ON_SALE(1, "在售"),

    OFF_SALE(0, "下架");

    private final int code;
    private final String text;

    SaleStatusEnum(int code, String text) {
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


    public static SaleStatusEnum instanceOf(Integer code) {
        if (code == null) {
            return OFF_SALE;
        }
        for (SaleStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return OFF_SALE;
    }
}
