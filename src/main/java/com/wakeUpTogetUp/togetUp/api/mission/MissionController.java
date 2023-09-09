package com.wakeUpTogetUp.togetUp.api.mission;

import com.wakeUpTogetUp.togetUp.api.auth.AuthUser;
import com.wakeUpTogetUp.togetUp.common.Status;
import com.wakeUpTogetUp.togetUp.common.dto.BaseResponse;
import com.wakeUpTogetUp.togetUp.api.file.FileService;
import com.wakeUpTogetUp.togetUp.api.mission.dto.request.PostMissionLogReq;
import com.wakeUpTogetUp.togetUp.api.mission.dto.response.GetMissionRes;
import com.wakeUpTogetUp.togetUp.api.mission.dto.response.MissionLogRes;
import com.wakeUpTogetUp.togetUp.api.mission.dto.response.PostObjectRecognitionRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "미션(Mission)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/app/mission")
public class MissionController {

    private final MissionProvider missionProvider;
    private final MissionService missionService;
    private final FileService fileService;

    @Operation(summary = "객체 인식 미션 목록 가져오기")
    @GetMapping("/object-detection")
    BaseResponse<GetMissionRes> getObjectDetectionMissions() {
        return new BaseResponse(Status.SUCCESS, missionProvider.getObjectDetectionMissions());
    }

    @Operation(summary = "표정 인식 미션 목록 가져오기")
    @GetMapping("/face-recognition")
    BaseResponse<GetMissionRes> getFaceRecognitionMissions() {
        return new BaseResponse(Status.SUCCESS, missionProvider.getFaceRecognitionMissions());
    }

    @Operation(summary = "객체 탐지 미션")
    @PostMapping(value = "/object-detection/{objectName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<PostObjectRecognitionRes> recognizeObject(
            @Parameter(hidden = true) @AuthUser Integer userId,
            @Parameter(description = "미션 수행 사진") @RequestPart MultipartFile missionImage,
            @Parameter(description = "탐지할 객체") @PathVariable String objectName
    ) throws Exception {
        long startTime = System.currentTimeMillis();

        missionService.recognizeObject(objectName, missionImage);
        String filePath = fileService.uploadFile(missionImage, "mission");

        // 걸린 시간 계산
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds: " + timeElapsed);

        return new BaseResponse(Status.MISSION_SUCCESS, new PostObjectRecognitionRes(filePath));
    }

    @Operation(summary = "미션 수행 기록 생성")
    @PostMapping("/complete")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<MissionLogRes> postMissionLog(
            @Parameter(hidden = true) @AuthUser Integer userId,
            @RequestBody @Valid PostMissionLogReq postMissionLogReq
    ) {
        missionService.createMissionLog(userId, postMissionLogReq);

        return new BaseResponse(Status.SUCCESS_CREATED);
    }

    // TODO : 달별 검색
    // TODO : 일별 검색
    @Operation(summary = "미션 수행 기록 가져오기")
    @GetMapping("/logs")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<MissionLogRes>> getMissionCompleteLogsByUserId(
            @Parameter(hidden = true) @AuthUser Integer userId
    ) {
        return new BaseResponse(Status.SUCCESS,
                missionProvider.getMissionCompleteLogsByUserId(userId));
    }
}
