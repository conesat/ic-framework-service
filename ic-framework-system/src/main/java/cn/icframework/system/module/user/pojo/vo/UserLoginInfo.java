package cn.icframework.system.module.user.pojo.vo;

import cn.icframework.common.consts.BasicLoginInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginInfo extends BasicLoginInfo {
    private String avatarFileUrl;
}
