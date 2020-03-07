package com.github.tuding.blindbox.infrastructure;

import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.domain.UserInfrastructure;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import com.github.tuding.blindbox.infrastructure.security.JWTTokenHandler;
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
    JWTTokenHandler jwtTokenHandler;

    @Override
    public Optional<String> getOpenId(String code) {
        return wxClient.getOpenId(code);
    }

    @Override
    public User saveUserWithOpenId(String openId) {
        //TODO: to implement the real save login
        return new User(openId);
    }

    @Override
    public String generateToken(User user) {
        return jwtTokenHandler.generateToken(user);
    }
}
