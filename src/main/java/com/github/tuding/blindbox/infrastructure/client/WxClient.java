package com.github.tuding.blindbox.infrastructure.client;


import com.github.tuding.blindbox.domain.Activity;
import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.util.HttpClientUtil;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import com.github.tuding.blindbox.infrastructure.util.WXBizDataCrypt;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    private final static String NOTIFICATION_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=ACCESS_TOKEN";
    private final static String TEMPLATE_ID = "iOYn0MAVCf5w9bdy5V3aA_jA_-f2xXt9uTRE5_pggt4";

    private static final String APPID = "APPID";
    private static final String APPSECRET = "APPSECRET";
    private static final String CODE = "CODE";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    private WxTokenResponse wxToken;

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

    private WxTokenResponse getWxToken() {
        if (wxToken.isTokenValid()) {
            return this.wxToken;
        } else {
            String requestUrl = TOKEN_URL.replace(APPID, appId).replace(APPSECRET, appSecret);
            String resultData = HttpClientUtil.instance().getData(requestUrl);
            final WxTokenResponse result = JsonUtil.toObject(resultData, WxTokenResponse.class);
            if (Strings.isNullOrEmpty(result.getAccess_token())) {
                log.warn("{}", resultData);
                throw new BizException(ErrorCode.FAIL_TO_GET_WXCHAT_ACCESS_TOKEN);
            } else {
                this.wxToken = result;
                return this.wxToken;
            }
        }
    }

    //TODO: need further update the template and map and page
    public void sendActivityNotify(String openId, Activity activity) {
        final String access_token = getWxToken().getAccess_token();

        Map<String, TemplateData> data = new HashMap<>();
        data.put("key1", new TemplateData(activity.getActivityName()));
        final WxNotifyRequest request = new WxNotifyRequest(access_token, activity.getNotifyJumpPage(),
                openId, TEMPLATE_ID, data);

        sendNotify(JsonUtil.toJson(request));
    }

    private void sendNotify(String request) {
        final String access_token = getWxToken().getAccess_token();
        String requestUrl = NOTIFICATION_URL.replace(ACCESS_TOKEN, access_token);
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=UTF-8");

            final Response response = HttpClientUtil.instance().postBody(requestUrl, request, headers);

            if (!response.isSuccessful()) {
                log.warn("fail to notify with response {}", response);
                log.warn(response.message());
            } else {
                log.info("Notification sent successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
