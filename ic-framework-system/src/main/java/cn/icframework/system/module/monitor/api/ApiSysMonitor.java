package cn.icframework.system.module.monitor.api;

import cn.icframework.auth.annotation.RequireAuth;
import cn.icframework.common.consts.Api;
import cn.icframework.core.common.bean.Response;
import cn.icframework.system.consts.UserType;
import cn.icframework.system.module.monitor.model.MonitorMetaResponse;
import cn.icframework.system.module.monitor.model.MonitorTimelineResponse;
import cn.icframework.system.module.monitor.service.SystemMonitorService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = Api.API_SYS + "/monitor", name = "资源监控")
@RequireAuth(userType = UserType.SYSTEM_USER, onlyToken = true)
public class ApiSysMonitor {
    private final SystemMonitorService monitorService;

    @GetMapping("/summary")
    public Response<MonitorTimelineResponse> summary() {
        return Response.success(monitorService.timeline(5));
    }

    @GetMapping("/timeline")
    public Response<MonitorTimelineResponse> timeline(@RequestParam(defaultValue = "15") @Min(1) @Max(1440) int minutes) {
        return Response.success(monitorService.timeline(minutes));
    }

    @GetMapping("/meta")
    public Response<MonitorMetaResponse> meta() {
        return Response.success(monitorService.meta());
    }
}
