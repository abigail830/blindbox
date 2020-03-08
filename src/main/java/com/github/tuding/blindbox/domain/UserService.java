package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserInfrastructure userInfrastructure;

    public String login(String code) {
        final String openId = userInfrastructure.getOpenId(code)
                .orElseThrow(() -> new BizException(ErrorCode.FAIL_TO_GET_OPENID));

        User user = userInfrastructure.saveUserWithOpenId(openId);
        log.info("{}", user);

        return userInfrastructure.generateToken(user);
    }
}
