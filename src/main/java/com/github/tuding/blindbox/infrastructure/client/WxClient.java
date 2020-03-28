package com.github.tuding.blindbox.infrastructure.client;


import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.util.HttpClientUtil;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import com.github.tuding.blindbox.infrastructure.util.WXBizDataCrypt;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class WxClient {

    private final static String LOGIN_MP_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID" +
            "&secret=APPSECRET" +
            "&grant_type=authorization_code" +
            "&js_code=CODE";

    private final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
            "&appid=APPID&secret=APPSECRET";
    private final static String NOTIFICATION_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/" +
            "send?access_token=";
    private final static String TEMPLATE_ID = "iOYn0MAVCf5w9bdy5V3aA_jA_-f2xXt9uTRE5_pggt4";
    private final static String MESSAGE_TEMPLATE = "您的【listName】-【wishName】被朋友领取啦！";
    private final static String COUPON_MESSAGE_TEMPLATE = "您有新的卡券,请登陆小程序领取！";

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

    public WxToken getWxToken() {
        String requestUrl = TOKEN_URL.replace(APPID, appId).replace(APPSECRET, appSecret);
        String resultData = HttpClientUtil.instance().getData(requestUrl);
        final WxToken result = JsonUtil.toObject(resultData, WxToken.class);
        if (Strings.isNullOrEmpty(result.getAccess_token())) {
            log.warn("{}", resultData);
            throw new BizException(ErrorCode.FAIL_TO_GET_WXCHAT_ACCESS_TOKEN);
        } else {
            return result;
        }
    }
}
