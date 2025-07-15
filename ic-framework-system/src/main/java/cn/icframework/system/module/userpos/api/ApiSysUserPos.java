package cn.icframework.system.module.userpos.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.userpos.pojo.dto.UserPosDTO;
import cn.icframework.system.module.userpos.pojo.vo.UserPosVO;
import cn.icframework.system.module.userpos.service.UserPosService;
import cn.icframework.system.module.userpos.wrapperbuilder.UserPosWrapperBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * manage接口，用于管理后台
 *
 * @author ic
 * @date 2024/09/09
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/user-pos", name = "用户职位")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysUserPos extends BasicApi {
    private final UserPosService userPosService;
    private final UserPosWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<UserPosVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(userPosService.selectById(id, UserPosVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<UserPosVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return userPosService.page(sqlWrapper, page, UserPosVO.class);
    }


    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        userPosService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated UserPosDTO dto) {
        userPosService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name = "新增")
    public Response<Void> create(@RequestParam Long posId, @RequestParam Long[] userIds) {
        userPosService.createBatch(posId, userIds);
        return Response.success();
    }
}
