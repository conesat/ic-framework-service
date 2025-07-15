package cn.icframework.hotel.common.enums;

import cn.icframework.common.interfaces.IEnum;

public enum SupportingTypeEnum implements IEnum {
    INFRASTRUCTURE(1, "基础设施"),
    BUSINESS(2, "商务设施"),
    DINING(3, "餐饮设施"),
    ENTERTAINMENT(4, "娱乐设施"),
    ACCOMMODATION(5, "客房设施"),
    SERVICE(6, "酒店服务");

    private final int code;
    private final String text;

    SupportingTypeEnum(int code, String text) {
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

    public static SupportingTypeEnum instanceOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (SupportingTypeEnum menuType : values()) {
            if (menuType.code == code) {
                return menuType;
            }
        }
        return null;
    }
}
