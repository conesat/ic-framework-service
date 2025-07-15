package cn.icframework.system.module.notice.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.notice.pojo.dto.NoticeDTO;
import cn.icframework.system.module.notice.pojo.vo.NoticeSimpleVO;
import cn.icframework.system.module.notice.pojo.vo.NoticeVO;
import cn.icframework.system.module.notice.pojo.vo.NoticeVOConverter;
import cn.icframework.system.module.notice.service.NoticeService;
import cn.icframework.system.module.notice.wrapperbuilder.NoticeWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/09/12
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/notice", name ="通知")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysNotice extends BasicApi {
    private final NoticeService noticeService;
    private final NoticeVOConverter noticeVOConverter;
    private final NoticeWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<NoticeVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(noticeService.selectById(id, NoticeVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<NoticeSimpleVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return noticeService.page(sqlWrapper, page, NoticeSimpleVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    //@PostMapping(value = "/all", name = "查询全部")
    //public List<NoticeVO> all(HttpServletRequest request) {
    //    SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
    //    return noticeVOConverter.convert(noticeService.select(sqlWrapper));
    //}

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        noticeService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated NoticeDTO dto) {
        dto.setUserId(JWTUtils.getUserId());
        noticeService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated NoticeDTO dto) {
        dto.setUserId(JWTUtils.getUserId());
        noticeService.edit(dto);
        return Response.success();
    }
}
