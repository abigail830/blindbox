package com.github.tuding.blindbox.infrastructure.client;


import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.infrastructure.util.HttpClientUtil;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import com.github.tuding.blindbox.infrastructure.util.WXBizDataCrypt;
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

    public Optional<User> getUerWithOpenIdAndSKey(String code) {
        String url = LOGIN_MP_URL.replace(APPID, appId).replace(APPSECRET, appSecret).replace(CODE, code);
        try {
            String resultData = HttpClientUtil.instance().getData(url);
            log.info("Getting wxchat response for code[{}]: {}", code, resultData);
            final WxLoginResponse wxLoginResponse = JsonUtil.toObject(resultData, WxLoginResponse.class);
            return Optional.ofNullable(wxLoginResponse.toUser());
        } catch (Exception ex) {
            return Optional.empty();
        }

    }


    public User decrypt(String skey, String encryptedData, String iv) {
        WXBizDataCrypt biz = new WXBizDataCrypt(appId, skey);
        String resultDate = biz.decryptData(encryptedData, iv);
        WxDecryptResponse response = JsonUtil.toObject(resultDate, WxDecryptResponse.class);
        return response.toUser();
    }
}
