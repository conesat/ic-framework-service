package cn.icframework.system.module.chatmsg.service;

import cn.icframework.core.basic.service.BasicService;
import cn.icframework.core.utils.BeanUtils;
import cn.icframework.system.module.chat.Chat;
import cn.icframework.system.module.chat.service.ChatService;
import cn.icframework.system.module.chatmsg.ChatMsg;
import cn.icframework.system.module.chatmsg.dao.ChatMsgMapper;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgDTO;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgSendDTO;
import cn.icframework.system.module.chatuser.def.ChatUserDef;
import cn.icframework.system.module.chatuser.service.ChatUserService;
import cn.icframework.system.module.user.service.IUserInfoProvider;
import cn.icframework.system.module.user.service.UserService;
import cn.icframework.system.module.ws.api.ApiWebSocket;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author ic
 * @date 2025/01/17
 */
@Service
@RequiredArgsConstructor
public class ChatMsgService extends BasicService<ChatMsgMapper, ChatMsg> {

    private final ChatService chatService;
    private final UserService userService;
    private final ChatUserService chatUserService;


    /**
     * 编辑或者保存
     *
     * @param dto
     */
    @Transactional
    public void edit(ChatMsgDTO dto) {
        ChatMsg entity = dto.getId() != null ? selectById(dto.getId()) : ChatMsg.def();
        BeanUtils.copyExcludeProps(dto, entity);
        if (dto.getId() != null) {
            updateById(entity);
        } else {
            insert(entity);
        }
    }

    @Override
    public void before(SqlCommandType sqlCommandType, ChatMsg entity) {
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            if (!StringUtils.hasLength(entity.getSummary())) {
                switch (entity.getMsgType()) {
                    case PIC -> entity.setSummary("[图片]");
                    case FILE -> entity.setSummary("[文件]");
                    case SHARE -> entity.setSummary("[分享]");
                    case VIDEO -> entity.setSummary("[视频]");
                    case VOICE -> entity.setSummary("[音频]");
                    case TEXT ->
                            entity.setSummary(entity.getMessage().length() <= 20 ? entity.getMessage() : (entity.getMessage().substring(0, 20) + "..."));
                }
            }
        }

    }

    @Override
    public void after(SqlCommandType sqlCommandType, ChatMsg entity) {
        if (sqlCommandType == SqlCommandType.INSERT) {
            // 找出所有聊天对象
            ChatUserDef chatUserDef = ChatUserDef.table();
            List<String> userIdList = chatUserService.select(SELECT(chatUserDef.userId).FROM(chatUserDef.chatId.eq(entity.getChatId())), String.class);
            IUserInfoProvider.BaseUserInfo info = userService.getUserInfo(entity.getUserType(), entity.getUserId());
            String userName = info.getUserName();
            String userPic = info.getUserPic();
            ChatMsgSendDTO msgDTO = new ChatMsgSendDTO();
            BeanUtils.copyProperties(entity, msgDTO);
            for (String userId : userIdList) {
                msgDTO.setToUserId(userId);
                msgDTO.setUserName(userName);
                msgDTO.setUserPic(userPic);
                ApiWebSocket.send(msgDTO);
            }
        }
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            // 更新聊天信息
            Chat chat = chatService.selectById(entity.getChatId());
            chat.setMessage(entity.getMessage());
            chat.setSummary(entity.getSummary());
            chat.setMsgType(entity.getMsgType());
            chat.setLastMsgTime(entity.getCreateTime());
            chatService.updateById(chat);
        }
    }
}
