package cn.icframework.system.module.chatuser.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.chatuser.ChatUser;
import cn.icframework.system.module.chatuser.pojo.dto.ChatUserDTO;
import cn.icframework.system.module.chatuser.pojo.vo.ChatUserVO;
import cn.icframework.system.module.chatuser.pojo.vo.ChatUserVOConverter;
import cn.icframework.system.module.chatuser.service.ChatUserService;
import cn.icframework.system.module.chatuser.wrapperbuilder.ChatUserWrapperBuilder;
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
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @since 2025/01/17
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/chat-user", name ="聊天用户关联")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysChatUser extends BasicApi {
    private final ChatUserService chatUserService;
    private final ChatUserVOConverter chatUserVOConverter;
    private final ChatUserWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<ChatUserVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(chatUserService.selectById(id, ChatUserVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ChatUserVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return chatUserVOConverter.convert(chatUserService.page(sqlWrapper, page, ChatUser.class));
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<ChatUserVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return chatUserVOConverter.convert(chatUserService.select(sqlWrapper));
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        chatUserService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated ChatUserDTO dto) {
        chatUserService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated ChatUserDTO dto) {
        chatUserService.edit(dto);
        return Response.success();
    }
}
