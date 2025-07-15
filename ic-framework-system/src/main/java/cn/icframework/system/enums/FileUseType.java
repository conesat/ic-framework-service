package cn.icframework.system.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * 文件用途
 * @author hzl
 * @date 2023/6/21
 */
public enum FileUseType implements IEnum {
    /**
     * 未指定类型
     */
    PRIVATE(-1, "private"),
    /**
     * 公开图片
     */
    PUBLIC(0, "public"),
    /**
     * 头像
     */
    AVATAR(1, "avatar"),
    /**
     * 配套设施图标
     */
    SUPPORTING_TYPE_ICON(2, "supportingTypeIcon"),
    /**
     * 酒店宣传图
     */
    HOTEL_PIC(3, "hotelPic"),
    /**
     * 酒店宣传图
     */
    PRODUCT_PIC(4, "product");

    private final int code;
    private final String text;

    FileUseType(int code, String text) {
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

    public static FileUseType instanceOf(Integer code) {
        if (code == null) {
            return PRIVATE;
        }
        for (FileUseType fileType : values()) {
            if (fileType.code == code) {
                return fileType;
            }
        }
        return PRIVATE;
    }
}
