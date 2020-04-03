package com.github.tuding.blindbox.infrastructure.client;

import com.github.tuding.blindbox.domain.order.PreOrder;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.util.HttpClientUtil;
import com.github.tuding.blindbox.infrastructure.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WxPayment {

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

    public PreOrder generatePayment(String openId, Product product, String orderId, String ipAddr) throws BizException {

        final WxPaymentRequest wxPaymentRequest = new WxPaymentRequest(appId, merchantId, KEY, product,
                orderId, ipAddr, CALL_BACK_URL, openId);
        final String xml = wxPaymentRequest.convertToXml();

        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", MediaType.APPLICATION_XML_VALUE);
            final Response response = HttpClientUtil.instance().postBody(PAY_URL, xml, headers);
            if (null != response.body()) {
                String result = response.body().string();
                Map<String, String> resultMap = parseXml(result);
                final WxPaymentResponse wxPaymentResponse = new WxPaymentResponse(resultMap, KEY);
                if (wxPaymentResponse.isSuccessPrePayment()) {
                    return wxPaymentResponse.toDomain();
                } else {
                    log.error("{}", wxPaymentResponse.getReturn());
                    throw new BizException(ErrorCode.FAIL_TO_PRE_ORDER);
                }
            } else {
                throw new BizException(ErrorCode.FAIL_TO_PRE_ORDER);
            }
        } catch (Exception e) {
            log.error("{}", e);
            throw new BizException(ErrorCode.FAIL_TO_PRE_ORDER);
        }
    }

    Map<String, String> parseXml(String xml) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        for (Element e : elementList)
            map.put(e.getName(), e.getText());
        return map;
    }


}
