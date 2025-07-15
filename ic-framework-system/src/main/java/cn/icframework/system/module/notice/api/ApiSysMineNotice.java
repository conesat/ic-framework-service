package cn.icframework.system.module.notice.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.basic.wrapperbuilder.QueryParams;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.notice.pojo.vo.NoticeMineVO;
import cn.icframework.system.module.notice.service.NoticeService;
import cn.icframework.system.module.notice.wrapperbuilder.MineNoticeWrapperBuilder;
import cn.icframework.system.module.notice.wrapperbuilder.NoticeWrapperBuilder;
import cn.icframework.system.module.noticereceiver.service.MineNoticeReceiverService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/09/12
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/mine/notice", name = "通知")
@RequireAuth(userType = UserType.SYSTEM_USER, onlyToken = true)
@RequiredArgsConstructor
public class ApiSysMineNotice extends BasicApi {
    private final NoticeService noticeService;
    private final MineNoticeReceiverService mineNoticeReceiverService;
    private final MineNoticeWrapperBuilder mineNoticeWrapperBuilder;

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<NoticeMineVO> page(HttpServletRequest request, PageRequest page, Boolean isRead) {
        QueryParams queryParams = getQueryParams(request);
        if (isRead != null) {
            if (isRead) {
                queryParams.put(MineNoticeWrapperBuilder.QUERY_FIELDS.USER_ID_READ, JWTUtils.getUserId());
            } else {
                queryParams.put(MineNoticeWrapperBuilder.QUERY_FIELDS.USER_ID_UNREAD, JWTUtils.getUserId());
            }
        } else {
            queryParams.put(MineNoticeWrapperBuilder.QUERY_FIELDS.USER_ID, JWTUtils.getUserId());
        }
        queryParams.put(NoticeWrapperBuilder.QUERY_FIELDS.ENABLE, true);
        SqlWrapper sqlWrapper = mineNoticeWrapperBuilder.build(queryParams);
        return noticeService.page(sqlWrapper, page, NoticeMineVO.class);
    }

    /**
     * 标记已读
     */
    @PutMapping(value = "/read", name = "标记已读")
    public Response<Void> read(@RequestParam("noticeId") List<Long> ids) {
        mineNoticeReceiverService.read(JWTUtils.getUserId(), ids);
        return Response.success();
    }


    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Long> ids) {
        mineNoticeReceiverService.delete(JWTUtils.getUserId(), ids);
        return Response.success();
    }
}
