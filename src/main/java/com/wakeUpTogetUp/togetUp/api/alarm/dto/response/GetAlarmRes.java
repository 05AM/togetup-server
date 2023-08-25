package com.wakeUpTogetUp.togetUp.api.alarm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "알람 1개 가져오기 response")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAlarmRes {
    @Schema(description = "알람 id")
    private Integer id;

    @Schema(description = "유저 id", example = "1")
    private Integer userId;

    @Schema(description = "미션 id", example = "1")
    private Integer missionId;

    @Schema(description = "알람 이름", example = "기상 알람")
    private String name;

    @Schema(description = "아이콘", example = "⏰")
    private String icon;

    @Schema(description = "진동 여부", example = "true")
    private Boolean isVibrate;

    @Schema(description = "다시울림 간격", example = "5")
    private Integer snoozeInterval;

    @Schema(description = "다시울림 횟수", example = "3")
    private Integer snoozeCnt;

    @Schema(description = "알람 시간", example = "06:00")
    private String alarmTime;

    @Schema(description = "월요일 울림 여부", example = "true")
    private Boolean monday;

    @Schema(description = "화요일 울림 여부", example = "true")
    private Boolean tuesday;

    @Schema(description = "수요일 울림 여부", example = "true")
    private Boolean wednesday;

    @Schema(description = "목요일 울림 여부", example = "true")
    private Boolean thursday;

    @Schema(description = "금요일 울림 여부", example = "true")
    private Boolean friday;

    @Schema(description = "토요일 울림 여부", example = "false")
    private Boolean saturday;

    @Schema(description = "일요일 울림 여부", example = "false")
    private Boolean sunday;

    @Schema(description = "알람 활성화 여부", example = "true")
    private Boolean isActivated;
}

