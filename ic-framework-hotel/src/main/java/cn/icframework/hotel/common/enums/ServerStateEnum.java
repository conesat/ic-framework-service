package cn.icframework.hotel.common.enums;

import cn.icframework.common.interfaces.IEnum;

/**
 * 通用状态
 *
 * @author hzl
 * @date 2023/6/21
 */
public enum ServerStateEnum implements IEnum {
    WAIT(0, "待接单"),
    WAIT_START(1, "待开始"),
    FINISH(2, "已完成"),

    DOING(3, "进行中");

    private final int code;
    private final String text;

    ServerStateEnum(int code, String text) {
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


    public static ServerStateEnum instanceOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (ServerStateEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
