package cn.icframework.system.module.setting.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.basic.api.BasicApi;
import cn.icframework.core.common.bean.Response;
import cn.icframework.system.enums.UserType;
import cn.icframework.system.module.setting.pojo.dto.SettingDTO;
import cn.icframework.system.module.setting.pojo.vo.SettingVO;
import cn.icframework.system.module.setting.pojo.vo.UserTypeVO;
import cn.icframework.system.module.setting.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ic generator
 * @date 2023/08/07
 */
@RestController
@RequestMapping(value = Api.API_SYS + "/setting", name = "系统设置")
@RequireAuth(userType = cn.icframework.system.consts.UserType.SYSTEM_USER)
@RequiredArgsConstructor
public class ApiSysSetting extends BasicApi {
    private final SettingService settingService;

    /**
     * 获取单个详情
     *
     * @return
     */
    @GetMapping(name = "获取详情")
    public Response<SettingVO> detail() {
        return Response.success(settingService.detail());
    }

    /**
     * 编辑或者保存
     */
    @PutMapping(name = "编辑")
    public Response<Void> edit(@Validated SettingDTO dto) {
        settingService.edit(dto);
        settingService.removeCache();
        return Response.success();
    }


    /**
     * 所有用户类型
     */
    @GetMapping(value = "/user-types", name = "所有用户类型")
    public Response<List<UserTypeVO>> UserTypes() {
        List<UserTypeVO> userTypes = new ArrayList<>();
        for (UserType value : UserType.values()) {
            userTypes.add(new UserTypeVO(value.code(), value.text()));
        }
        return Response.success(userTypes);
    }
}
