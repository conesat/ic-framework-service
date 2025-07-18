package cn.icframework.system.module.position.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.position.pojo.dto.PositionDTO;
import cn.icframework.system.module.position.pojo.vo.PositionVO;
import cn.icframework.system.module.position.service.PositionService;
import cn.icframework.system.module.position.wrapperbuilder.PositionWrapperBuilder;
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
 * @since 2024/09/09
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/position", name ="职位")
 @RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysPosition extends BasicApi {
    private final PositionService positionService;
    private final PositionWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<PositionVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(positionService.selectById(id, PositionVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<PositionVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return positionService.page(sqlWrapper, page, PositionVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        positionService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated PositionDTO dto) {
        positionService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated PositionDTO dto) {
        positionService.edit(dto);
        return Response.success();
    }
}
