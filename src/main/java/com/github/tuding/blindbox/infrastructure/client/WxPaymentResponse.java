package com.github.tuding.blindbox.infrastructure.client;

import com.github.tuding.blindbox.domain.order.PreOrder;
import com.google.common.base.Strings;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
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

    public PreOrder toDomain() {
        String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id +
                "&signType=MD5&timeStamp=" + preOrderTime;

        //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
        String paySign = sign(stringSignTemp, key, "utf-8").toUpperCase();

        return new PreOrder(prepay_id, nonce_str, preOrderTime, paySign);
    }


    private String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    private byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public String getReturn() {
        return "Return Info: {" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                '}';
    }
}
