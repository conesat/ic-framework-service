package cn.icframework.system.module.chat.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.utils.Assert;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.enums.ChatType;
import cn.icframework.system.module.chat.Chat;
import cn.icframework.system.module.chat.dao.ChatMapper;
import cn.icframework.system.module.chat.pojo.dto.ChatDTO;
import cn.icframework.system.module.chat.pojo.vo.ChatWithUserVO;
import cn.icframework.system.module.chat.wrapperbuilder.ChatWrapperBuilder;
import cn.icframework.system.module.chatuser.ChatUser;
import cn.icframework.system.module.chatuser.service.ChatUserService;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.service.SysFileService;
import cn.icframework.system.module.user.User;
import cn.icframework.system.module.user.pojo.vo.UserSimpleVO;
import cn.icframework.system.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author ic
 * @date 2025/01/17
 */
@Service
@RequiredArgsConstructor
public class ChatService extends BasicService<ChatMapper, Chat> {
    private final UserService userService;
    private final ChatUserService chatUserService;
    private final SysFileService sysFileService;
    private final ChatWrapperBuilder wrapperBuilder;

    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(ChatDTO dto) {
        Chat entity = dto.getId() != null ? selectById(dto.getId()) : Chat.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    /**
     * 创建一对一聊天
     * 系统用户
     *
     * @param subject
     * @param userId
     */
    public Chat createSysSingle(String subject, String userId) {
        User user = userService.selectById(userId);
        Assert.isNotNull(user, "用户不存在");

        Chat chat = getChat(subject, userId);
        if (chat != null) return chat;
        chat = Chat.def();
        chat.setChatType(ChatType.USER);
        chat.setOwnerId(subject);
        insert(chat);

        ChatUser chatMy = ChatUser.def();
        chatMy.setChatId(chat.getId());
        chatMy.setUserId(subject);
        chatMy.setUserType(UserType.SYSTEM_USER);
        chatUserService.insert(chatMy);

        ChatUser chatUser = ChatUser.def();
        chatUser.setChatId(chat.getId());
        chatUser.setUserId(userId);
        chatUser.setUserType(UserType.SYSTEM_USER);
        chatUserService.insert(chatUser);
        return chat;
    }

    /**
     * 获取已有聊天
     *
     * @param subject
     * @param userId
     * @return
     */
    @Nullable
    private Chat getChat(String subject, String userId) {
        return selectOne(wrapperBuilder.chat(subject, userId));
    }

    @Override
    public void before(SqlCommandType sqlCommandType, Chat entity) {
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            Chat old = selectById(entity.getId());
            // 新增或者头像变化调整地址
            if (old == null || !Objects.equals(old.getAvatarFileId(), entity.getAvatarFileId())) {
                SysFile sysFile = sysFileService.selectById(entity.getAvatarFileId());
                if (sysFile != null) {
                    entity.setAvatarFileUrl(sysFile.getBucketUrl() + "/" + sysFile.getOssObjectName());
                    sysFileService.addRef(entity.getAvatarFileId());
                }
                if (old != null) {
                    sysFileService.removeRef(old.getAvatarFileId());
                }
            }
        }
    }

    /**
     * 查询聊天并且返回用户信息
     *
     * @param subject
     * @param params
     * @param page
     * @return
     */
    public PageResponse<ChatWithUserVO> pageWithUser(String subject, QueryParams params, PageRequest page) {
        List<ChatWithUserVO> chatList = select(wrapperBuilder.chatList(subject, params, page), ChatWithUserVO.class);
        for (ChatWithUserVO chatWithUserVO : chatList) {
            chatWithUserVO.setUsers(select(wrapperBuilder.top4(chatWithUserVO.getId(), subject), UserSimpleVO.class));
        }
        return page.toResponse(chatList);
    }
}

