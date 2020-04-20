package com.github.tuding.blindbox.infrastructure.client.payment;

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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    @ResponseBody
    String paymentCallback(HttpServletRequest request, HttpServletResponse response) {
        final WxPayCallbackReq wxPayCallbackReq = getRequestInObject(request);
        try {
            if (!wxPayCallbackReq.isSuccessReq() || !wxPayCallbackReq.validSign(merchantSecret) ||
                    !wxPayCallbackReq.isValidParam(appId, merchantId, TRADETYPE)) {
                log.warn("Invalid sign or param, will return fail for wx callback");
                returnFail(response);
                return null;
            }
        } catch (Exception e) {
            throw new BizException(ErrorCode.INVALID_SIGN);
        }

        String orderId = wxPayCallbackReq.getOutTradeNo();
        if (wxPayCallbackReq.isSuccessPay()) {
            orderService.updateOrderToPaySuccess(orderId);
            log.info("Updated Pay success for order {}", orderId);
        } else {
            orderService.updateOrderToPayFail(orderId);
            Order order = orderService.getOrder(orderId);
            drawService.cancelADrawByDrawId(order.getDrawId());
            log.info("Updated Pay fail for order {}, and cancel draw {}", orderId, order.getDrawId());
        }
        returnSuccess(response);
        return null;
    }

    @PostMapping(value = "/transport/callback")
    @Transactional
    String tranportCallback(HttpServletRequest request, HttpServletResponse response) {
        final WxPayCallbackReq wxPayCallbackReq = getRequestInObject(request);

        try {
            if (!wxPayCallbackReq.isSuccessReq() || !wxPayCallbackReq.validSign(merchantSecret) ||
                    !wxPayCallbackReq.isValidParam(appId, merchantId, TRADETYPE)) {
                log.warn("Invalid sign or param, will return fail for wx callback");
                returnFail(response);
            }
        } catch (Exception e) {
            throw new BizException(ErrorCode.INVALID_SIGN);
        }

        if (wxPayCallbackReq.isSuccessPay()) {
            orderService.updateOrderToTransportPaySuccess(wxPayCallbackReq.getOutTradeNo());
            log.info("Updated Pay success for order {}", wxPayCallbackReq.getOutTradeNo());
        } else {
            orderService.updateOrderToTransportPayFail(wxPayCallbackReq.getOutTradeNo());
            log.info("Updated Pay fail for order {}", wxPayCallbackReq.getOutTradeNo());
        }
        returnSuccess(response);

        return null;
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

    void returnFail(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final String result = "<xml>\n" +
                "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                "  <return_msg><![CDATA[]]></return_msg>\n" +
                "</xml>";
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void returnSuccess(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        final String result = "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
