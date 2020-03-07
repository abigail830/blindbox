package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    WxClient wxClient;

    @Autowired
    UserInfrastructure userInfrastructure;

    public String login(String code) {
        final String openId = wxClient.getOpenId(code)
                .orElseThrow(() -> new BizException(ErrorCode.FAIL_TO_GET_OPENID));

        return userInfrastructure.saveUserWithOpenId(openId);
    }
}
