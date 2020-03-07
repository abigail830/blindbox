package com.github.tuding.blindbox.infrastructure.client;


import com.github.tuding.blindbox.infrastructure.util.HttpClientUtil;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class WxClient {

    static final String LOGIN_MP_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID" +
            "&secret=APPSECRET" +
            "&grant_type=authorization_code" +
            "&js_code=CODE";
    private static final String APPID = "APPID";
    private static final String APPSECRET = "APPSECRET";
    private static final String CODE = "CODE";
    @Value("${app.appId}")
    private String appId;

    @Value("${app.appSecret}")
    private String appSecret;

    public Optional<String> getOpenId(String code) {
        String url = LOGIN_MP_URL
                .replace(APPID, appId).replace(APPSECRET, appSecret).replace(CODE, code);
        String resultData = HttpClientUtil.instance().getData(url);
        log.debug("Getting wxchat response for code[{}]: {}", code, resultData);

        WxLoginResponse wxLoginResponse = JsonUtil.toObject(resultData, WxLoginResponse.class);
        return Optional.ofNullable(wxLoginResponse.getOpenid());
    }


}
