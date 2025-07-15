package cn.icframework.system.module.noticereceiver.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.noticereceiver.pojo.vo.NoticeReceiverVO;
import cn.icframework.system.module.noticereceiver.pojo.vo.NoticeReceiverVOConverter;
import cn.icframework.system.module.noticereceiver.service.NoticeReceiverService;
import cn.icframework.system.module.noticereceiver.wrapperbuilder.NoticeReceiverWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/09/13
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/notice-receiver", name ="通知接收对象")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysNoticeReceiver extends BasicApi {
    private final NoticeReceiverService noticeReceiverService;
    private final NoticeReceiverVOConverter noticeReceiverVOConverter;
    private final NoticeReceiverWrapperBuilder wrapperBuilder;

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<NoticeReceiverVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return noticeReceiverService.page(sqlWrapper, page, NoticeReceiverVO.class);
    }

    /**
     * 批量添加用户
     */
    @PostMapping(value = "/add-user-batch",name ="批量添加用户")
    public Response<Void> addUserBatch(@Validated Long noticeId, @Validated Long[] userIds) {
        noticeReceiverService.addUserBatch(noticeId, userIds);
        return Response.success();
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("userIds") List<Long> userIds, @RequestParam("noticeId") Long noticeId) {
        noticeReceiverService.delete(noticeId, userIds);
        return Response.success();
    }
}
