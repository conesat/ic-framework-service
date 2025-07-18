package cn.icframework.system.module.user.pojo.vo;

import cn.icframework.core.basic.pojo.BasicConverter;
import cn.icframework.system.module.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author hzl
 * @since 2023/6/13
 */
@Component
@RequiredArgsConstructor
public class UserVOConverter extends BasicConverter<User, UserVO> {
}
