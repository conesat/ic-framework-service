package cn.icframework.system.module.setting.api;

import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.Response;
import cn.icframework.auth.standard.ISystemVerifyService;
import cn.icframework.system.module.setting.pojo.dto.InitDTO;
import cn.icframework.system.module.setting.pojo.vo.ActivationInfoVO;
import cn.icframework.system.module.setting.pojo.vo.AdVO;
import cn.icframework.system.module.setting.pojo.vo.AppAdVO;
import cn.icframework.system.module.setting.pojo.vo.SettingVO;
import cn.icframework.system.module.setting.service.SettingService;
import cn.icframework.system.module.setting.service.SystemVerifyServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author hzl
 * @date 2023/6/5
 */
@RestController
@RequestMapping(value = Api.API_PUBLIC + "/setting", name = "系统设置")
@AllArgsConstructor
public class ApiPublicSetting extends BasicApi {
    private final SettingService settingService;
    private final SystemVerifyServiceImpl systemVerifyService;

    @GetMapping("/is-init")
    public ISystemVerifyService.StatusEnum isInit() {
        return systemVerifyService.validate();
    }

    @PostMapping("/init")
    public ActivationInfoVO init(@Validated InitDTO initDTO) {
        return settingService.init(initDTO);
    }

    /**
     * 修改激活码
     */
    @PostMapping(value = "update-code",name ="修改激活码")
    public Response<Void> updateCode(@RequestParam("code") String code) {
        settingService.updateActivationCode(code);
        settingService.removeCache();
        return Response.success();
    }

    /**
     * 获取广告详情
     */
    @GetMapping(value = "ad",name = "获取广告详情")
    public Response<AdVO> adDetail() {
        SettingVO detail = settingService.detail();
        AdVO adVO = new AdVO();
        if (detail != null) {
            adVO.setAdFileUrl(detail.getAdFileUrl());
            adVO.setAdUrl(detail.getAdUrl());
            adVO.setAdType(detail.getAdType());
        }
        return Response.success(adVO);
    }


    /**
     * 获取广告详情
     */
    @GetMapping(value = "app-ad", name = "获取广告详情")
    public Response<AppAdVO> appAdDetail() {
        SettingVO detail = settingService.detail();
        AppAdVO appAdVO = new AppAdVO();
        appAdVO.setAppAdFileUrl(detail.getAppAdFileUrl());
        appAdVO.setAppAdUrl(detail.getAppAdUrl());
        appAdVO.setAppAdType(detail.getAppAdType());
        return Response.success(appAdVO);
    }
}
