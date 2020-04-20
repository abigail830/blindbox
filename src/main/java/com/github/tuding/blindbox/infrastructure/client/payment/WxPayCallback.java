package com.github.tuding.blindbox.infrastructure.client.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderService;
import com.github.tuding.blindbox.domain.product.DrawService;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    @PostMapping(value = "/product/callback")
    @Transactional
    String paymentCallback(HttpServletRequest request, HttpServletResponse response) {

        final WxPayCallbackReq wxPayCallbackReq = getRequestInObject(request);
        log.info("WxPayCallbackReq [{}]", wxPayCallbackReq);

        if (!wxPayCallbackReq.isSuccessReq() || !validSign(wxPayCallbackReq) ||
                !wxPayCallbackReq.isValidParam(appId, merchantId, TRADETYPE)) {
            return fail(response);
        }

        String orderId = wxPayCallbackReq.getOut_trade_no();
        if (wxPayCallbackReq.isSuccessPay()) {
            orderService.updateOrderToPaySuccess(orderId);
        } else {
            orderService.updateOrderToPayFail(orderId);
            Order order = orderService.getOrder(orderId);
            drawService.cancelADrawByDrawId(order.getDrawId());
        }
        return success(response);
    }

    @PostMapping(value = "/transport/callback")
    @Transactional
    String tranportCallback(HttpServletRequest request, HttpServletResponse response) {
        final WxPayCallbackReq wxPayCallbackReq = getRequestInObject(request);
        log.info("WxPayCallbackReq [{}]", wxPayCallbackReq);

        if (!wxPayCallbackReq.isSuccessReq() || !validSign(wxPayCallbackReq) ||
                !wxPayCallbackReq.isValidParam(appId, merchantId, TRADETYPE)) {
            return fail(response);
        }

        if (wxPayCallbackReq.isSuccessPay()) {
            orderService.updateOrderToTransportPaySuccess(wxPayCallbackReq.getOut_trade_no());
        } else {
            orderService.updateOrderToTransportPayFail(wxPayCallbackReq.getOut_trade_no());
        }
        return success(response);
    }

    private WxPayCallbackReq getRequestInObject(HttpServletRequest request) {
        try {
            Map<String, String> map = XmlUtil.xmlToMap(readRequest(request));
            log.info("Map from HttpServletRequest {}", map);
            return new WxPayCallbackReq(map);
        } catch (IOException e) {
            throw new BizException(ErrorCode.IO_EXCEPTION);
        } catch (Exception ex) {
            throw new BizException(ErrorCode.WX_USER_NOT_FOUND);
        }
    }

    String fail(HttpServletResponse response) {
        response.setHeader("Content-type", "text/html");
        response.setCharacterEncoding("UTF-8");
        return "<xml>\n" +
                "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                "  <return_msg><![CDATA[]]></return_msg>\n" +
                "</xml>";
    }

    String success(HttpServletResponse response) {
        response.setHeader("Content-type", "text/html");
        response.setCharacterEncoding("UTF-8");
        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }

    public String readRequest(HttpServletRequest request) throws IOException {
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String str;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((str = in.readLine()) != null) {
            sb.append(str);
        }
        in.close();
        inputStream.close();
        return sb.toString();
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
