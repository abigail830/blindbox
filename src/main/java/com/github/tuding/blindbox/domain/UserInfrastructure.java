package com.github.tuding.blindbox.domain;

import java.util.Optional;

public interface UserInfrastructure {

    Optional<String> getOpenId(String code);

    void saveUserWithOpenId(String openId);

    String generateToken(String openId);

    String getOpenIdFromToken(String token);

    Optional<User> getUserByOpenId(String token);
}
