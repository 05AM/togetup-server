package com.wakeUpTogetUp.togetUp.api.dev;


import com.wakeUpTogetUp.togetUp.api.users.UserRepository;
import com.wakeUpTogetUp.togetUp.api.users.domain.User;
import com.wakeUpTogetUp.togetUp.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DevService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String devGetJwt(Integer userId) {
        return jwtService.generateAccessToken(userId);
    }
}
