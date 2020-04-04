package com.github.tuding.blindbox.infrastructure.client.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tuding.blindbox.domain.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/wx/payment")
public class WxPayCallback {

    @Value("${app.appId}")
    private String appId;

    @Value("${app.mchId}")
    private String merchantId;

    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADETYPE = "JSAPI";
    @Autowired
    OrderService orderService;
    @Value("${app.mchSecret}")
    private String merchantSecret;

    @PostMapping(value = "/callback",
            produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    WxPayCallbackRes paymentCallback(@RequestBody WxPayCallbackReq wxPayCallbackReq) {
        log.info("{}", wxPayCallbackReq);

        if (!wxPayCallbackReq.isSuccessReq() || !validSign(wxPayCallbackReq) ||
                !wxPayCallbackReq.isValidParam(appId, merchantId, TRADETYPE)) {
            return WxPayCallbackRes.buildFail("参数格式校验错误");
        }

        //TODO: to update order DB record
        if (wxPayCallbackReq.isSuccessPay()) {

        } else {

        }
        return WxPayCallbackRes.buildSuccess();
    }

    @SuppressWarnings("unchecked")
    private Boolean validSign(WxPayCallbackReq wxPayCallbackReq) {
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(wxPayCallbackReq, Map.class);

        if (map.containsKey("sign")) {
            map.remove("sign");
            final Map<String, String> stringMap = convertToString(map);
            final String signMe = SignUtil.sign(stringMap, merchantSecret);
            return signMe.equals(wxPayCallbackReq.getSign());
        } else {
            return Boolean.FALSE;
        }
    }

    private Map<String, String> convertToString(Map<String, Object> map) {
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                newMap.put(entry.getKey(), (String) entry.getValue());
            } else {
                newMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return newMap;
    }


}
