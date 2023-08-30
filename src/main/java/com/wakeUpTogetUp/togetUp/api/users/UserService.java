package com.wakeUpTogetUp.togetUp.api.users;

import com.wakeUpTogetUp.togetUp.api.alarm.AlarmRepository;
import com.wakeUpTogetUp.togetUp.api.auth.AuthUser;
import com.wakeUpTogetUp.togetUp.api.room.RoomUserRepository;
import com.wakeUpTogetUp.togetUp.api.users.fcmToken.FcmToken;
import com.wakeUpTogetUp.togetUp.api.users.fcmToken.FcmTokenRepository;
import com.wakeUpTogetUp.togetUp.api.users.model.User;
import com.wakeUpTogetUp.togetUp.common.Status;
import com.wakeUpTogetUp.togetUp.exception.BaseException;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final RoomUserRepository roomUserRepository;
    private final AlarmRepository alarmRepository;


    public Integer updateFcmToken(Integer userId, Integer fcmTokenId, String fcmToken) {

        User user = userRepository.findById(userId).orElse(null);
        FcmToken fcmTokenObject;
        if (Objects.isNull(user))
            new BaseException(Status.USER_NOT_FOUND);
        if(fcmTokenId==null)
        {
             fcmTokenObject =  FcmToken.builder()
                    .value(fcmToken)
                    .user(user)
                    .build();
        }else {
             fcmTokenObject = fcmTokenRepository.findById(fcmTokenId).orElse(
                    FcmToken.builder()
                            .value(fcmToken)
                            .user(user)
                            .build());
            fcmTokenObject.updateFcmToken(fcmToken);
        }

      return fcmTokenRepository.save(fcmTokenObject).getId();

    }

    public void updateAgreePush(Integer userId, boolean agreePush){
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(Status.USER_NOT_FOUND));
        user.setAgreePush(agreePush);
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(Integer userId){
        userRepository.findById(userId).orElseThrow(() -> new BaseException(Status.USER_NOT_FOUND));
        userRepository.deleteById(userId);

        //roomUser 삭제
        Integer roomUserNumber = roomUserRepository.countByUserId(userId);

        if(roomUserNumber>0)
            roomUserRepository.deleteByUserId(userId);

        //알람 삭제
        Integer alarmNumber =alarmRepository.countByUserId(userId);

        if(alarmNumber>0)
            alarmRepository.deleteByUserIdAndRoomIdIsNull(userId);
    }

}
