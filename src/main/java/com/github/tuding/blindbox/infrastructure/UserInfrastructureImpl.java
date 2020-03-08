package com.github.tuding.blindbox.infrastructure;

import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.domain.UserInfrastructure;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import com.github.tuding.blindbox.infrastructure.security.JwtTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UserInfrastructureImpl implements UserInfrastructure {

    @Autowired
    WxClient wxClient;

    @Autowired
    JwtTokenHandler jwtTokenHandler;

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<String> getOpenId(String code) {
        return wxClient.getOpenId(code);
    }

    @Override
    public void saveUserWithOpenId(String openId) {
        userRepository.saveUserWithOpenId(openId);
    }

    @Override
    public String generateToken(String openId) {
        String token = jwtTokenHandler.generateWxToken(openId);
        log.info("token for openId[{}] is {}", openId, token);
        return token;
    }

    @Override
    public Optional<User> getUserByOpenId(String openId) {
        return userRepository.getUserByOpenId(openId);
    }

    @Override
    public String getOpenIdFromToken(String token) {
        return jwtTokenHandler.verifyWxToken(token).getSubject();
    }
}
