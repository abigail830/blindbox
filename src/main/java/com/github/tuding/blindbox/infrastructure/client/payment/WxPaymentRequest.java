package com.github.tuding.blindbox.infrastructure.client.payment;

import com.github.tuding.blindbox.domain.order.Order;
import com.google.common.base.Strings;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WxPaymentRequest {

    //小程序ID
    private String appid;
    //商户号
    private String mch_id;
    //随机字符串
    private String nonce_str;
    //签名
    private String sign;
    //商品描述
    private String body;
    //商户订单号
    private String out_trade_no;
    //标价金额
    private Integer total_fee;
    //终端IP
    private String spbill_create_ip;
    //通知地址
    private String notify_url;
    //交易类型
    private String trade_type = WxPayment.TRADETYPE;
    private String key;
    private String openId;

    public WxPaymentRequest(String appid, String merchantId, String merchantSecret, String notifyUrl,
                            String ip, Order order) {
        this.appid = appid;
        this.mch_id = merchantId;
        this.key = merchantSecret;
        this.openId = order.getOpenId();
        this.nonce_str = getRandomStringByLength(32);
        this.out_trade_no = order.getOrderId();
        this.body = !Strings.isNullOrEmpty(order.getProductName()) ? order.getProductName() : null;
        this.total_fee = (order.getProductPrice() != null) ?
                order.getProductPrice().multiply(BigDecimal.valueOf(100)).intValue() : 0;
        this.spbill_create_ip = ip;
        this.notify_url = notifyUrl;
    }

    public String convertToXmlWithSign() {
        return "<xml>" + "<appid>" + appid + "</appid>"
                + "<body><![CDATA[" + body + "]]></body>"
                + "<mch_id>" + mch_id + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + notify_url + "</notify_url>"
                + "<openid>" + openId + "</openid>"
                + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<trade_type>" + trade_type + "</trade_type>"
                + "<sign>" + generateSign() + "</sign>"
                + "</xml>";
    }

    private String generateSign() {
        this.sign = SignUtil.sign(toPaymentMap(), this.key).toUpperCase();
        return this.sign;
    }

    private Map<String, String> toPaymentMap() {
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", this.appid);
        packageParams.put("mch_id", this.mch_id);
        packageParams.put("nonce_str", this.nonce_str);
        packageParams.put("body", this.body);
        packageParams.put("out_trade_no", this.out_trade_no);
        packageParams.put("total_fee", this.total_fee.toString());
        packageParams.put("spbill_create_ip", this.spbill_create_ip);
        packageParams.put("notify_url", this.notify_url);
        packageParams.put("trade_type", this.trade_type);
        packageParams.put("openid", this.openId);
        return packageParams;
    }

    private String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
