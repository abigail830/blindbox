package com.github.tuding.blindbox.infrastructure.client.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
public class WxPayCallbackReq {

    Map<String, String> map;

    public WxPayCallbackReq(Map<String, String> map) {
        this.map = map;
    }

    public String createSign(SortedMap<String, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        return encodeMD5(sb.toString());
    }

    public boolean validSign(String key) throws Exception {
        String signWx = map.getOrDefault("sign", null);
        if (signWx == null) return false;
        map.remove("sign"); // 需要去掉原 map 中包含的 sign 字段再进行签名
        String signMe = createSign(new TreeMap<String, Object>(map), key);
        log.info("Compare SignWX[{}] & SignMe[{}]", signWx, signMe);
        return signWx.equals(signMe);
    }

    public String encodeMD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = (str).getBytes();
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }


    public Boolean isSuccessReq() {
        return "SUCCESS".equals(map.get("return_code"));
    }

    public Boolean isSuccessPay() {
        return "SUCCESS".equals(map.get("result_code"));
    }

    public Boolean isValidParam(String appId, String mchId, String tradeType) {
        return appId.equals(map.get("appid")) &&
                mchId.equals(map.get("mch_id")) &&
                tradeType.equals(map.get("trade_type"));
    }

    public String getOutTradeNo() {
        return map.get("out_trade_no");
    }


}
