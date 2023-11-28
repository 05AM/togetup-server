package com.wakeUpTogetUp.togetUp.api.notification;

import com.wakeUpTogetUp.togetUp.api.notification.dto.request.BroadCastNotificationReq;
import com.wakeUpTogetUp.togetUp.api.notification.dto.request.PushNotificationReq;
import com.wakeUpTogetUp.togetUp.api.notification.dto.response.NotificationRes;
import com.wakeUpTogetUp.togetUp.api.notification.dto.response.PushNotificationRes;
import com.wakeUpTogetUp.togetUp.common.Status;
import com.wakeUpTogetUp.togetUp.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "알림(Notification)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/app/notification")
public class NotificationController {

    private final NotificationService notificationService;

    //    private final NotificationProvider notificationProvider;
    @Operation(summary = "전체 유저에게 fcm 노티 전송")
    @PostMapping("/broadcast")
    public  BaseResponse<Status> sendNotificationToAllUsers(
            @Valid @RequestBody BroadCastNotificationReq broadCastNotificationReq
    ) {
        notificationService.sendNotificationToAllUsers(broadCastNotificationReq.getTitle(), broadCastNotificationReq.getBody());

        return new BaseResponse(Status.SUCCESS);
    }




//    @Operation(summary = "토큰으로 알림 보내기")
//    @PostMapping("/token")
//    BaseResponse<PushNotificationRes> sendPushAlarmToToken(
//            @RequestBody PushNotificationReq request
//    ) throws ExecutionException, InterruptedException {
//        return new BaseResponse<>(Status.SUCCESS, fcmService.sendMessageToToken(request));
//    }

//    @PostMapping("/tokens")
//    BaseResponse<PushNotificationRes> sendPushAlarmToTokens(
//            @RequestBody PushNotificationReq request
//    ) throws ExecutionException, InterruptedException {
//        return new BaseResponse<>(Status.SUCCESS, fcmService.sendMessageToTokens(request));
//    }

//    @PostMapping("/topic")
//    BaseResponse<PushNotificationRes> sendPushAlarmToTopic(
//            @RequestBody PushNotificationReq request
//    ) throws ExecutionException, InterruptedException {
//        return new BaseResponse<>(Status.SUCCESS, fcmService.sendMessageToTopic(request));
//    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/group/{groupId}")
//    BaseResponse<PushNotificationRes> sendPushAlarmToGroup(
//            @PathVariable Integer groupId,
//            @RequestBody PushNotificationReq request
//    ) throws ExecutionException, InterruptedException {
//        return new BaseResponse<>(Status.SUCCESS_CREATED, notificationService.sendMessageToGroup(groupId, request));
//    }

    // 유저 알림 목록 가져오기
//    @GetMapping("/user/{userId}")
//    BaseResponse<List<NotificationRes>> getUserPushLogList(
//            @PathVariable Integer userId
//    ){
//        return new BaseResponse<>(Status.SUCCESS, notificationProvider.getUserPushLogList(userId));
//    }
}
