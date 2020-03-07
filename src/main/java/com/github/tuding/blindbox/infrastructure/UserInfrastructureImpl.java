package com.github.tuding.blindbox.infrastructure;

import com.github.tuding.blindbox.domain.UserInfrastructure;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserInfrastructureImpl implements UserInfrastructure {

    @Autowired
    WxClient wxClient;

    @Override
    public Optional<String> getOpenId(String code) {
        return wxClient.getOpenId(code);
    }

    @Override
    public String saveUserWithOpenId(String openId) {
        //TODO: to implement the real save login
        return UUID.randomUUID().toString();
    }
}
