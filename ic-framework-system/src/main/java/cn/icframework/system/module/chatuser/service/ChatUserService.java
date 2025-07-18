package cn.icframework.system.module.chatuser.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.chatuser.ChatUser;
import cn.icframework.system.module.chatuser.dao.ChatUserMapper;
import cn.icframework.system.module.chatuser.pojo.dto.ChatUserDTO;
import cn.icframework.system.module.chatuser.wrapperbuilder.ChatUserWrapperBuilder;
import cn.icframework.system.module.user.pojo.vo.UserSimpleVO;
import cn.icframework.system.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ic
 * @since 2025/01/17
 */
@Service
@RequiredArgsConstructor
public class ChatUserService extends BasicService<ChatUserMapper, ChatUser> {
    final private UserService userService;
    final private ChatUserWrapperBuilder wrapperBuilder;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(ChatUserDTO dto) {
        ChatUser entity = dto.getId() != null ? selectById(dto.getId()) : new ChatUser();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    /**
     * 获取聊天组内用户
     *
     * @param chatId
     * @param searchKey
     * @return
     */
    public List<UserSimpleVO> allUser(String chatId, String searchKey) {
        return userService.select(wrapperBuilder.allUser(chatId, searchKey), UserSimpleVO.class);
    }
}
