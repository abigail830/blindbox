package com.github.tuding.blindbox.domain;

import java.util.Optional;

public interface UserInfrastructure {

    Optional<String> getOpenId(String code);

    User saveUserWithOpenId(String openId);

    String generateToken(User user);
}
