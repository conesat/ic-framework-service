package cn.icframework.system.module.chatmsg.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chatmsg.pojo.dto.ChatMsgDTO;
import cn.icframework.system.module.chatmsg.pojo.vo.ChatMsgVO;
import cn.icframework.system.module.chatmsg.pojo.vo.ChatMsgVOConverter;
import cn.icframework.system.module.chatmsg.service.ChatMsgService;
import cn.icframework.system.module.chatmsg.wrapperbuilder.ChatMsgWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2025/01/17
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/chat-msg", name ="消息")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysChatMsg extends BasicApi {
    private final ChatMsgService msgService;
    private final ChatMsgVOConverter msgVOConverter;
    private final ChatMsgWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<ChatMsgVO> detail(@PathVariable("id") Serializable id) {
        ChatMsgVO msgVO = msgService.selectById(id, ChatMsgVO.class);
        msgVOConverter.handlerBaseUserInfo(Collections.singletonList(msgVO));
        return Response.success(msgVO);
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ChatMsgVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        PageResponse<ChatMsgVO> pageResponse = msgService.page(sqlWrapper, page, ChatMsgVO.class);
        msgVOConverter.handlerBaseUserInfo(pageResponse.getRecords());
        return pageResponse;
    }

    /**
     * 查询全部
     *
     * @return
     */
    //@PostMapping(value = "/all", name = "查询全部")
    //public List<MsgVO> all(HttpServletRequest request) {
    //    SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
    //    return msgVOConverter.convert(msgService.select(sqlWrapper));
    //}

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        msgService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated ChatMsgDTO dto) {
        msgService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated ChatMsgDTO dto) {
        msgService.edit(dto);
        return Response.success();
    }
}
