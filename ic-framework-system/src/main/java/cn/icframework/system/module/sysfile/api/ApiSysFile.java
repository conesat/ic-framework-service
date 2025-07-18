package cn.icframework.system.module.sysfile.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.PageRequest;
import cn.icframework.core.common.bean.PageResponse;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.utils.JWTUtils;
import cn.icframework.mybatis.wrapper.SqlWrapper;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.enums.FileUseType;
import cn.icframework.system.module.sysfile.SysFile;
import cn.icframework.system.module.sysfile.pojo.dto.SysFileDTO;
import cn.icframework.system.module.sysfile.pojo.vo.SysFileVO;
import cn.icframework.system.module.sysfile.pojo.vo.SysFileVOConverter;
import cn.icframework.system.module.sysfile.service.OssFileHelper;
import cn.icframework.system.module.sysfile.service.SysFileService;
import cn.icframework.system.module.sysfile.wrapperbuilder.SysFileWrapperBuilder;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * @author ic generator
 * @since 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/sys-file", name = "系统文件")
@RequireAuth(userType = UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysFile extends BasicApi {
    private final SysFileService sysFileService;
    private final SysFileVOConverter sysFileVOConverter;
    private final SysFileWrapperBuilder wrapperBuilder;
    private final OssFileHelper ossFileHelper;


    /**
     * 获取单个详情
     *
     * @param id [Serializable] *id
     * @return
     */
    @GetMapping(value = "/item/{id}", name = "获取详情")
    public Response<SysFileVO> detail(@PathVariable("id") Serializable id) {
        return Response.success(sysFileVOConverter.convert(sysFileService.selectById(id)));
    }

    /**
     * 获取列表
     *
     * @param pageIndex  [int] 当前页码
     * @param pageSize [int] 分页大小
     * @return
     */
    @PostMapping(value = "/page", name = "分页查询")
    public PageResponse<SysFileVO> page(HttpServletRequest request, PageRequest page) {
        SqlWrapper sqlWrapper = wrapperBuilder.build(getQueryParams(request));
        PageResponse<SysFile> pageResponse = sysFileService.page(sqlWrapper, page);
        return sysFileVOConverter.convert(pageResponse);
    }

    /**
     * 删除
     *
     * @param ids [Serializable[]] id列表
     * @return
     */
    @DeleteMapping(name = "删除")
    public Response<Void> delete(@RequestParam("ids") List<Serializable> ids) {
        sysFileService.deleteByIds(ids);
        return Response.success();
    }

    /**
     * 编辑或者保存
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated SysFileDTO dto) {
        sysFileService.edit(dto);
        return Response.success();
    }

    /**
     * 单文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequireAuth(onlyToken = true)
    @PostMapping(value = "/upload-single", name = "单文件上传")
    public Response<SysFileVO> uploadSingle(MultipartFile file, @RequestParam("useType") FileUseType useType) throws Exception {
        return Response.success(sysFileVOConverter.convert(ossFileHelper.uploadSingle(file, useType, JWTUtils.getUserId())));
    }

    /**
     * 上传切片
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequireAuth(onlyToken = true)
    @PostMapping(value = "/upload-slice", name = "切片上传")
    public Response<SysFileVO> uploadSlice(MultipartFile file, @RequestParam("uploadId") String uploadId, @RequestParam("partNumber") Integer partNumber) throws Exception {
        return Response.success(sysFileVOConverter.convert(ossFileHelper.upload(file, uploadId, partNumber)));
    }

    /**
     * 上传切片
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequireAuth(onlyToken = true)
    @GetMapping(value = "/register-upload-slice", name = "切片上传")
    public Response<String> registerUploadSlice(@RequestParam("useType") FileUseType useType, @RequestParam("fileName") String fileName, @RequestParam("totalParts") Integer totalParts) {
        return Response.success(ossFileHelper.registerUploadSlice(useType, fileName, totalParts, JWTUtils.getUserId()));
    }

    /**
     * 取消分片上传
     *
     * @param uploadId
     * @return
     */
    @RequireAuth(onlyToken = true)
    @PutMapping(value = "/abort-upload-slice", name = "切片上传")
    public Response<Void> abortUploadSlice(@RequestParam("uploadId") String uploadId) {
        ossFileHelper.abortUploadSlice(uploadId);
        return Response.success();
    }
}
