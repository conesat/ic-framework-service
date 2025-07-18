package cn.icframework.system.module.dic.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.dic.pojo.dto.DicDTO;
import cn.icframework.system.module.dic.pojo.vo.DicVO;
import cn.icframework.system.module.dic.service.DicService;
import cn.icframework.system.module.dic.wrapperbuilder.DicWrapperBuilder;
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
 * @author ic generator
 * @since 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/dic", name ="字典")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysDic extends BasicApi {
    private final DicService dicService;
    private final DicWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<DicVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(dicService.selectById(id, DicVO.class));
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        dicService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑或者保存
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated DicDTO dto) {
        dicService.edit(dto);
        return Response.success();
    }

    /**
     * 保存
     */
    @PostMapping(name ="编辑")
    public Response<Void> create(@Validated DicDTO dto) {
        dicService.edit(dto);
        return Response.success();
    }

    /**
     * 编辑状态
     */
    @PostMapping(value = "/change-enable", name ="编辑状态")
    public Response<Void> changeEnable(@RequestParam("ids") List<Serializable> ids, @RequestParam("enable") Boolean enable) {
        dicService.changeEnable(ids, enable);
        return Response.success();
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<DicVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return dicService.page(sqlWrapper, page, DicVO.class);
    }
}
