package com.github.tuding.blindbox.infrastructure.client.payment;

import com.github.tuding.blindbox.domain.order.Order;
import com.google.common.base.Strings;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WxPaymentResponse {

    private static final String SUCCESS = "SUCCESS";

    private String return_code;
    private String return_msg;

    private String result_code;
    private String appid;
    private String mch_id;
    private String sign;
    private String nonce_str;

    //交易类型
    private String trade_type;
    //预支付交易会话标识
    private String prepay_id;
    //二维码链接
    private String code_url;

    private String err_code;
    private String err_code_des;

    private String key;
    private String preOrderTime;

    public WxPaymentResponse(Map<String, String> returnMap, String key) {
        this.return_code = returnMap.getOrDefault("return_code", "");
        this.return_msg = returnMap.getOrDefault("return_msg", "");

        this.result_code = returnMap.getOrDefault("result_code", "");
        this.appid = returnMap.getOrDefault("appid", "");
        this.mch_id = returnMap.getOrDefault("mch_id", "");
        this.sign = returnMap.getOrDefault("sign", "");
        this.nonce_str = returnMap.getOrDefault("nonce_str", "");

        this.trade_type = returnMap.getOrDefault("trade_type", "");
        this.prepay_id = returnMap.getOrDefault("prepay_id", "");
        this.code_url = returnMap.getOrDefault("code_url", "");
        this.err_code = returnMap.getOrDefault("err_code", "");
        this.err_code_des = returnMap.getOrDefault("err_code_des", "");
        this.key = key;
        this.preOrderTime = System.currentTimeMillis() + "";
    }

    public Boolean isSuccessPrePayment() {
        return this.result_code.equals(SUCCESS) && !Strings.isNullOrEmpty(result_code) &&
                this.return_code.equals(SUCCESS) && !Strings.isNullOrEmpty(return_code);
    }

    public Order addWxPayInfo(Order order, String appid, String key) {
        order.updateWxPayInfo(prepay_id, nonce_str, preOrderTime, appid, key);
        return order;
    }


    public String getReturn() {
        return "Return Info: {" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                '}';
    }
}
