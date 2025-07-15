package cn.icframework.system.module.user.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户信息提供接口
 */
public interface IUserInfoProvider {

    @Getter
    @Setter
    class BaseUserInfo {
        private String userId;
        private String userName;
        private String userPic;
        private String userType;
    }

    /**
     * 获取用户基本信息
     *
     * @param userType
     * @param userId
     * @return
     */
    BaseUserInfo getInfo(String userType, String userId);

    /**
     * 获取用户基本信息
     *
     * @param userType
     * @param userIdList
     * @return
     */
    List<BaseUserInfo> getInfo(String userType, List<String> userIdList);
}
