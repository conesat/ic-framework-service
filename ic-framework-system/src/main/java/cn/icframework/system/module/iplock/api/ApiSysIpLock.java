package cn.icframework.system.module.iplock.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.iplock.IpLock;
import cn.icframework.system.module.iplock.pojo.dto.IpLockDTO;
import cn.icframework.system.module.iplock.pojo.vo.IpLockVO;
import cn.icframework.system.module.iplock.pojo.vo.IpLockVOConverter;
import cn.icframework.system.module.iplock.service.IpLockService;
import cn.icframework.system.module.iplock.wrapperbuilder.IpLockWrapperBuilder;
import jakarta.annotation.Resource;
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
 * @since 2025/07/22
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/ip-lock", name ="ip登录锁")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysIpLock extends BasicApi {
    private final IpLockService ipLockService;
    private final IpLockVOConverter ipLockVOConverter;
    private final IpLockWrapperBuilder wrapperBuilder;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<IpLockVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(ipLockService.selectById(id, IpLockVO.class));
    }

    /**
     * 获取列表
     *
     * @param current  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<IpLockVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        return ipLockService.page(sqlWrapper, page, IpLockVO.class);
    }

    /**
     * 查询全部
     *
     * @return
     */
    //@PostMapping(value = "/all", name = "查询全部")
    //public List<IpLockVO> all(HttpServletRequest request) {
    //    SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
    //    return ipLockService.select(sqlWrapper, IpLockVO.class);
    //}

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        ipLockService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑
     */
    @PutMapping(name ="编辑")
    public Response<Void> edit(@Validated IpLockDTO dto) {
        ipLockService.edit(dto);
        return Response.success();
    }

    /**
     * 新增
     */
    @PostMapping(name ="新增")
    public Response<Void> create(@Validated IpLockDTO dto) {
        ipLockService.edit(dto);
        return Response.success();
    }
}
