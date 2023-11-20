package com.wakeUpTogetUp.togetUp.api.home;

import com.wakeUpTogetUp.togetUp.api.alarm.AlarmService;
import com.wakeUpTogetUp.togetUp.api.alarm.dto.response.AlarmTimeLineRes;
import com.wakeUpTogetUp.togetUp.api.auth.AuthUser;
import com.wakeUpTogetUp.togetUp.common.Status;
import com.wakeUpTogetUp.togetUp.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "홈(home)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/app/home")
public class HomeController {
    private final AlarmService alarmService;

    @Operation(summary = "브리핑 보드 - 알람 타임라인")
    @GetMapping("/brief-board/alarm/timeline")
    BaseResponse<AlarmTimeLineRes> getAlarmTimeLineOfUser(
            @Parameter(hidden = true) @AuthUser Integer userId
    ) {
        return new BaseResponse<>(Status.SUCCESS, alarmService.getAlarmTimeLineByUserId(userId));
    }
}
