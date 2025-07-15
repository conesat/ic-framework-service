package cn.icframework.hotel.module.consumeitem.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.hotel.module.consumeitem.pojo.dto.ConsumeItemDTO;
import cn.icframework.hotel.module.consumeitem.pojo.vo.ConsumeItemVO;
import cn.icframework.hotel.module.consumeitem.pojo.vo.ConsumeItemVOConverter;
import cn.icframework.hotel.module.consumeitem.service.ConsumeItemService;
import cn.icframework.hotel.module.consumeitem.wrapperbuilder.ConsumeItemWrapperBuilder;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
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
 * @date 2024/10/14
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/consume-item", name ="消费项目")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysConsumeItem extends BasicApi {
    private final ConsumeItemService consumeItemService;
    private final ConsumeItemVOConverter consumeItemVOConverter;
    private final ConsumeItemWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<ConsumeItemVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(consumeItemService.selectById(id, ConsumeItemVO.class));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<ConsumeItemVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return consumeItemService.page(sqlWrapper, page, ConsumeItemVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @PostMapping(value = "/all", name = "查询全部")
    public List<ConsumeItemVO> all(HttpServletRequest request) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return consumeItemService.select(sqlWrapper, ConsumeItemVO.class);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        consumeItemService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated ConsumeItemDTO dto) {
        consumeItemService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated ConsumeItemDTO dto) {
        consumeItemService.edit(dto);
        return Response.success();
    }
}
