package com.wakeUpTogetUp.togetUp.api.alarm;

import com.wakeUpTogetUp.togetUp.api.alarm.dto.request.PatchAlarmReq;
import com.wakeUpTogetUp.togetUp.api.alarm.dto.request.PostAlarmReq;
import com.wakeUpTogetUp.togetUp.api.alarm.model.Alarm;
import com.wakeUpTogetUp.togetUp.api.mission.repository.MissionObjectRepository;
import com.wakeUpTogetUp.togetUp.api.mission.repository.MissionRepository;
import com.wakeUpTogetUp.togetUp.api.mission.model.Mission;
import com.wakeUpTogetUp.togetUp.api.mission.model.MissionObject;
import com.wakeUpTogetUp.togetUp.api.users.UserRepository;
import com.wakeUpTogetUp.togetUp.api.users.model.User;
import com.wakeUpTogetUp.togetUp.common.Status;
import com.wakeUpTogetUp.togetUp.exception.BaseException;
import java.time.LocalTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wakeUpTogetUp.togetUp.api.users.UserServiceHelper.*;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final MissionObjectRepository missionObjectRepository;

    @Deprecated
    @Transactional
    public Alarm createAlarmDeprecated(Integer userId, PostAlarmReq postAlarmReq) {
        User user = findExistingUser(userRepository, userId);

        Mission mission = null;
        MissionObject missionObject = null;

        if (postAlarmReq.getMissionId() != null) {
            mission = missionRepository.findById(postAlarmReq.getMissionId())
                    .orElseThrow(() -> new BaseException(Status.MISSION_NOT_FOUND));

            if (postAlarmReq.getMissionObjectId() != null) {
                missionObject = missionObjectRepository.findById(postAlarmReq.getMissionObjectId())
                        .orElseThrow(() -> new BaseException(Status.MISSION_OBJECT_NOT_FOUND));

                if (missionObject.getMission().getId() != mission.getId()) {
                    throw new BaseException(Status.MISSION_ID_NOT_MATCH);
                }
            }
        }

        Alarm alarm = Alarm.create(
                postAlarmReq.getName(),
                missionObject.getIcon(),
                postAlarmReq.getSnoozeInterval(),
                postAlarmReq.getSnoozeCnt(),
                LocalTime.parse(postAlarmReq.getAlarmTime()),
                postAlarmReq.getMonday(),
                postAlarmReq.getTuesday(),
                postAlarmReq.getWednesday(),
                postAlarmReq.getThursday(),
                postAlarmReq.getFriday(),
                postAlarmReq.getSaturday(),
                postAlarmReq.getSunday(),
                postAlarmReq.getIsSnoozeActivated(),
                postAlarmReq.getIsVibrate(),
                user, mission, missionObject);

        return alarmRepository.save(alarm);
    }

    @Transactional
    public Alarm updateAlarm(Integer userId, Integer alarmId, PatchAlarmReq patchAlarmReq) {
        // 알람 수정
        Alarm alarm = alarmRepository.findById(alarmId, userId)
                .orElseThrow(() -> new BaseException(Status.ALARM_NOT_FOUND));
        Mission mission = null;
        MissionObject missionObject = null;

        if (patchAlarmReq.getMissionId() != null) {
            mission = missionRepository.findById(patchAlarmReq.getMissionId())
                    .orElseThrow(() -> new BaseException(Status.MISSION_NOT_FOUND));

            if (patchAlarmReq.getMissionObjectId() != null) {
                missionObject = missionObjectRepository.findById(patchAlarmReq.getMissionObjectId())
                        .orElseThrow(() -> new BaseException(Status.MISSION_NOT_FOUND));

                if (!Objects.equals(missionObject.getMission().getId(), mission.getId())) {
                    throw new BaseException(Status.MISSION_ID_NOT_MATCH);
                }
            }
        }

        alarm.modifyProperties(
                patchAlarmReq.getName(),
                patchAlarmReq.getIcon(),
                patchAlarmReq.getSnoozeInterval(),
                patchAlarmReq.getSnoozeCnt(),
                LocalTime.parse(patchAlarmReq.getAlarmTime()),
                patchAlarmReq.getMonday(),
                patchAlarmReq.getTuesday(),
                patchAlarmReq.getWednesday(),
                patchAlarmReq.getThursday(),
                patchAlarmReq.getFriday(),
                patchAlarmReq.getSaturday(),
                patchAlarmReq.getSunday(),
                patchAlarmReq.getIsVibrate(),
                patchAlarmReq.getIsSnoozeActivated(),
                patchAlarmReq.getIsActivated(),
                mission,
                missionObject
        );

        return alarmRepository.save(alarm);
    }

    @Transactional
    public int deleteAlarm(Integer alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new BaseException(Status.ALARM_NOT_FOUND));
        alarmRepository.delete(alarm);

        return alarm.getId();
    }
}
