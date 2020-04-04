package com.github.tuding.blindbox.infrastructure.client.payment;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WxPayment {

    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String CALL_BACK_URL = "http://47.104.146.206/wx/payment/product/callback";

    @Value("${app.appId}")
    private String appId;

    @Value("${app.appSecret}")
    private String appSecret;

    @Value("${app.mchId}")
    private String merchantId;

    @Value("${app.mchSecret}")
    private String merchantSecret;

    public Order generatePayment(Order order, String ipAddr) throws BizException {

        final WxPaymentRequest wxPaymentRequest = new WxPaymentRequest(appId, merchantId, merchantSecret,
                CALL_BACK_URL, ipAddr, order);
        final String xml = wxPaymentRequest.convertToXmlWithSign();

        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", MediaType.APPLICATION_XML_VALUE);
            final Response response = HttpClientUtil.instance().postBody(PAY_URL, xml, headers);

            if (null != response.body()) {
                Map<String, String> resultMap = XmlUtil.xmlToMap(response.body().string());
                final WxPaymentResponse wxPaymentResponse = new WxPaymentResponse(resultMap, merchantSecret);
                if (wxPaymentResponse.isSuccessPrePayment()) {
                    return wxPaymentResponse.toOrderWithPayInfo(order, appId, merchantSecret);
                } else {
                    log.error("{}", wxPaymentResponse.getReturn());
                    throw new BizException(ErrorCode.FAIL_TO_PLACE_ORDER);
                }
            } else {
                log.warn("Fail to get response from wxchat payment");
                throw new BizException(ErrorCode.FAIL_TO_PLACE_ORDER);
            }
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL_TO_PLACE_ORDER);
        }
    }



}
