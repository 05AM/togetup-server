package com.wakeUpTogetUp.togetUp.missions;

import com.wakeUpTogetUp.togetUp.alarms.AlarmRepository;
import com.wakeUpTogetUp.togetUp.alarms.model.Alarm;
import com.wakeUpTogetUp.togetUp.common.Status;
import com.wakeUpTogetUp.togetUp.common.dto.BaseResponse;
import com.wakeUpTogetUp.togetUp.exception.BaseException;
import com.wakeUpTogetUp.togetUp.missions.dto.request.PostMissionCompleteLogReq;
import com.wakeUpTogetUp.togetUp.missions.dto.response.MissionCompleteLogRes;
import com.wakeUpTogetUp.togetUp.missions.model.MissionCompleteLog;
import com.wakeUpTogetUp.togetUp.objectDetection.ObjectDetection;
import com.wakeUpTogetUp.togetUp.users.UserRepository;
import com.wakeUpTogetUp.togetUp.users.model.User;
import com.wakeUpTogetUp.togetUp.utils.mapper.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class MissionService {
    private ObjectDetection objectDetection = new ObjectDetection();
    private final MissionCompleteLogRepository missionCompleteLogRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public boolean recognizeObject(String object, MultipartFile missionImage) {
        // 일치 여부 확인
        boolean isConsistent = false;
        for(String objectDetected : objectDetection.detectObject(missionImage)){
            //TODO : 지우기
            System.out.println(objectDetected);

            if(objectDetected.equals(object))
                isConsistent = true;
        }

        // 일치하지 않으면
        if(!isConsistent)
            throw new BaseException(Status.MISSION_UNSUCCESS);
        else
            return isConsistent;
    }

    // 미션 수행 기록하기
    @Transactional
    public Integer createMissionCompleteLog(PostMissionCompleteLogReq req){
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(
                        () -> new BaseException(Status.INVALID_USER_ID));

        Alarm alarm = alarmRepository.findById(req.getAlarmId())
                .orElseThrow(
                        () -> new BaseException(Status.INVALID_ALARM_ID));

        MissionCompleteLog missionCompleteLog = MissionCompleteLog.builder()
                .user(user)
                .alarm(alarm)
                .title(req.getTitle())
                .picLink(req.getPicLink())
                .type(req.getType())
                .build();

        MissionCompleteLog missionCompleteLogCreated = missionCompleteLogRepository.save(missionCompleteLog);

        return missionCompleteLogCreated.getId();
    }
}

