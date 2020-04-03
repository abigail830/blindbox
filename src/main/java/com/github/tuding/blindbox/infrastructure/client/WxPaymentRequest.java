package com.github.tuding.blindbox.infrastructure.client;

import com.github.tuding.blindbox.domain.product.Product;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WxPaymentRequest {

    //小程序ID
    String appid;
    //商户号
    String mch_id;
    //随机字符串
    String nonce_str;
    //签名
    String sign;
    //商品描述
    String body;
    //商户订单号
    String out_trade_no;
    //标价金额
    Integer total_fee;
    //终端IP
    String spbill_create_ip;
    //通知地址
    String notify_url;
    //交易类型
    String trade_type = WxPayment.TRADETYPE;
    String openid;
    private String key;
    private String openId;

    public WxPaymentRequest(String appid, String mch_id, String key, Product product, String out_trade_no,
                            String spbill_create_ip, String notify_url, String openId) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.key = key;
        this.openId = openId;
        this.nonce_str = getRandomStringByLength(32);
        this.body = product.getName();
        this.out_trade_no = out_trade_no;
        this.total_fee = product.getPrice().multiply(BigDecimal.valueOf(100)).intValue();
        this.spbill_create_ip = spbill_create_ip;
        this.notify_url = notify_url;
    }


    public String convertToXml() {
        setSign();
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
                + "<sign>" + sign + "</sign>"
                + "</xml>";
    }

    void setSign() {
        final Map<String, String> paymentMap = toPaymentMap();
        final String link = createLinkString(paymentMap);
        this.sign = sign(link, this.key, "utf-8").toUpperCase();
    }

    String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
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
        packageParams.put("openid", this.openid);
        return packageParams;
    }

    String sign(String text, String key, String input_charset) {
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


}
