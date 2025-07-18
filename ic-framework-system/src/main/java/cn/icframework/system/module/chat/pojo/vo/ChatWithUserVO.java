package cn.icframework.system.module.chat.pojo.vo;

import cn.icframework.mybatis.annotation.Join;
import cn.icframework.mybatis.annotation.Joins;
import cn.icframework.system.module.user.pojo.vo.UserSimpleVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ic
 * @since 2025/01/17
 */
@Getter
@Setter
public class ChatWithUserVO extends ChatVO {
    /**
     * 用户
     */
    private List<UserSimpleVO> users;
}
