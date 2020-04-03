package com.github.tuding.blindbox.infrastructure.client;

import com.github.tuding.blindbox.infrastructure.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequestMapping("/wx/payment")
public class WxPaymentCallback {

    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADETYPE = "JSAPI";
    //微信支付的商户密钥
    public static final String KEY = "KEY";
    //微信统一下单接口地址
    public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String CALL_BACK_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    @Autowired
    IpUtil ipUtil;
    @Value("${app.appId}")
    private String appId;
    @Value("${app.appSecret}")
    private String appSecret;
    @Value("${app.mchId}")
    private String merchantId;

    @PostMapping("/callback")
    void paymentCallback(HttpServletRequest request) {
        log.info("{}", request);
    }


}
