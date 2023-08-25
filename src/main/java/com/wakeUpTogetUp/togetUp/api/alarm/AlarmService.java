package com.wakeUpTogetUp.togetUp.api.alarm;

import com.wakeUpTogetUp.togetUp.api.alarm.dto.request.PatchAlarmReq;
import com.wakeUpTogetUp.togetUp.api.alarm.dto.response.GetAlarmRes;
import com.wakeUpTogetUp.togetUp.api.alarm.model.Alarm;
import com.wakeUpTogetUp.togetUp.api.alarm.dto.request.PostAlarmReq;
import com.wakeUpTogetUp.togetUp.exception.BaseException;
import com.wakeUpTogetUp.togetUp.common.Status;
import com.wakeUpTogetUp.togetUp.api.mission.MissionRepository;
import com.wakeUpTogetUp.togetUp.api.mission.model.Mission;
import com.wakeUpTogetUp.togetUp.api.users.UserRepository;
import com.wakeUpTogetUp.togetUp.api.users.model.User;
import com.wakeUpTogetUp.togetUp.utils.mapper.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;

    // 알람 생성
    @Transactional
    public GetAlarmRes createAlarm(Integer userId, PostAlarmReq postAlarmReq) {
        // alarm 생성
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(Status.INVALID_USER_ID));

        Mission mission = missionRepository.findById(postAlarmReq.getMissionId())
                .orElseThrow(() -> new BaseException(Status.INVALID_MISSION_ID));

        Alarm alarm = Alarm.builder()
                .user(user)
                .mission(mission)
                .name(postAlarmReq.getName())
                .icon(postAlarmReq.getIcon())
                .isVibrate(postAlarmReq.getIsVibrate())
                .snoozeInterval(postAlarmReq.getSnoozeInterval())
                .snoozeCnt(postAlarmReq.getSnoozeCnt())
                .alarmTime(postAlarmReq.getAlarmTime())
                .monday(postAlarmReq.getMonday())
                .tuesday(postAlarmReq.getTuesday())
                .wednesday(postAlarmReq.getWednesday())
                .thursday(postAlarmReq.getThursday())
                .friday(postAlarmReq.getFriday())
                .saturday(postAlarmReq.getSaturday())
                .sunday(postAlarmReq.getSunday())
                .build();

        // 영속성 컨텍스트의 특성으로 default 값으로 넣은 속성은 값이 null 로 나오는 것 같다.
        Alarm alarmCreated = alarmRepository.save(alarm);

        return EntityDtoMapper.INSTANCE.toAlarmRes(alarmCreated);
    }

    // 알람 수정
    @Transactional
    public GetAlarmRes updateAlarm(Integer userId, Integer alarmId, PatchAlarmReq patchAlarmReq) {
        // 알람 수정
        Alarm alarm = alarmRepository.findById(alarmId, userId)
                .orElseThrow(() -> new BaseException(Status.INVALID_ALARM_ID));

        Mission mission = missionRepository.findById(patchAlarmReq.getMissionId())
                .orElseThrow(() -> new BaseException(Status.INVALID_MISSION_ID));

        alarm.modifyProperties(
                mission,
                patchAlarmReq.getName(),
                patchAlarmReq.getIcon(),
                patchAlarmReq.getIsVibrate(),
                patchAlarmReq.getSnoozeInterval(),
                patchAlarmReq.getSnoozeCnt(),
                patchAlarmReq.getAlarmTime(),
                patchAlarmReq.getMonday(),
                patchAlarmReq.getTuesday(),
                patchAlarmReq.getWednesday(),
                patchAlarmReq.getThursday(),
                patchAlarmReq.getFriday(),
                patchAlarmReq.getSaturday(),
                patchAlarmReq.getSunday(),
                patchAlarmReq.getIsActivated());
        Alarm alarmModified = alarmRepository.save(alarm);

        return EntityDtoMapper.INSTANCE.toAlarmRes(alarmModified);
    }

    // 알람 삭제
    @Transactional
    public void deleteAlarm(Integer alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new BaseException(Status.INVALID_ALARM_ID));

        alarmRepository.delete(alarm);
    }
}