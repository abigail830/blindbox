package com.github.tuding.blindbox.infrastructure.client.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderService;
import com.github.tuding.blindbox.domain.product.DrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADETYPE = "JSAPI";

    @Value("${app.appId}")
    private String appId;

    @Value("${app.mchId}")
    private String merchantId;
    @Autowired
    DrawService drawService;

    @Autowired
    OrderService orderService;
    @Value("${app.mchSecret}")
    private String merchantSecret;

    @PostMapping(value = "/product/callback",
            produces = "text/xml;charset=UTF-8", consumes = "text/xml;charset=UTF-8")
    @ResponseBody
    @Transactional
    WxPayCallbackRes paymentCallback(@RequestBody WxPayCallbackReq wxPayCallbackReq) {
        log.info("{}", wxPayCallbackReq);

        if (!wxPayCallbackReq.isSuccessReq() || !validSign(wxPayCallbackReq) ||
                !wxPayCallbackReq.isValidParam(appId, merchantId, TRADETYPE)) {
            return WxPayCallbackRes.buildFail("参数格式校验错误");
        }

        String orderId = wxPayCallbackReq.getOut_trade_no();
        if (wxPayCallbackReq.isSuccessPay()) {
            orderService.updateOrderToPaySuccess(orderId);
        } else {
            orderService.updateOrderToPayFail(orderId);
            Order order = orderService.getOrder(orderId);
            drawService.cancelADrawByDrawId(order.getDrawId());
        }
        return WxPayCallbackRes.buildSuccess();
    }

    @PostMapping(value = "/transport/callback",
            produces = "text/xml;charset=UTF-8", consumes = "text/xml;charset=UTF-8")
    @ResponseBody
    @Transactional
    WxPayCallbackRes tranportCallback(@RequestBody WxPayCallbackReq wxPayCallbackReq) {
        log.info("{}", wxPayCallbackReq);

        if (!wxPayCallbackReq.isSuccessReq() || !validSign(wxPayCallbackReq) ||
                !wxPayCallbackReq.isValidParam(appId, merchantId, TRADETYPE)) {
            return WxPayCallbackRes.buildFail("参数格式校验错误");
        }

        if (wxPayCallbackReq.isSuccessPay()) {
            orderService.updateOrderToTransportPaySuccess(wxPayCallbackReq.getOut_trade_no());
        } else {
            orderService.updateOrderToTransportPayFail(wxPayCallbackReq.getOut_trade_no());
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
            log.warn("Wx callback does not contain sign");
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
