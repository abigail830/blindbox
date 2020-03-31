package com.github.tuding.blindbox.infrastructure.client;

import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.security.AESUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WXBizDataCrypt {

    private String appid;

    private String sessionKey;

    public WXBizDataCrypt(String appid, String sessionKey) {
        this.appid = appid;
        this.sessionKey = sessionKey;
    }


    public String decryptData(String encryptedData, String iv) {

        Map<String, Object> map = new HashMap<>();

        byte[] aesKey = getAesKey();
        byte[] aesIV = getAesIv(iv);
        byte[] aesCipher = Base64.decodeBase64(encryptedData);

        try {
            byte[] resultByte = AESUtils.decrypt(aesCipher, aesKey, aesIV);

            if (null != resultByte && resultByte.length > 0) {
                return validateWaterMark(resultByte);
            } else {
                throw new BizException(ErrorCode.FAIL_TO_DECRYPT);
            }
        } catch (InvalidAlgorithmParameterException | UnsupportedEncodingException e) {
            throw new BizException(ErrorCode.FAIL_TO_DECRYPT);
        }
    }

    private String validateWaterMark(byte[] resultByte) throws UnsupportedEncodingException {
        String userInfo = new String(resultByte, "UTF-8");

        // watermark参数说明：
        // 参数  类型  说明
        // watermark   OBJECT  数据水印
        // appid   String  敏感数据归属appid，开发者可校验此参数与自身appid是否一致
        // timestamp   DateInt 敏感数据获取的时间戳, 开发者可以用于数据时效性校验'

        // 根据微信建议：敏感数据归属appid，开发者可校验此参数与自身appid是否一致
        // if decrypted['watermark']['appid'] != self.appId:
        JsonObject jsons = new JsonParser().parse(userInfo).getAsJsonObject();
        String id = jsons.getAsJsonObject("watermark").get("appid").getAsString();
        if (!StringUtils.equals(id, appid)) {
            throw new BizException(ErrorCode.ILLEGAL_BUFFER);
        } else {
            log.info("User info decrypted: {}", userInfo);
            return userInfo;
        }
    }

    private byte[] getAesIv(String iv) {
        if (StringUtils.length(iv) != 24) {
            throw new BizException(ErrorCode.INVALID_IV);
        }

        // 对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
        return Base64.decodeBase64(iv);
    }

    private byte[] getAesKey() {
        if (StringUtils.length(sessionKey) != 24) {
            throw new BizException(ErrorCode.INVALID_AES_KEY);
        }
        // 对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
        return Base64.decodeBase64(sessionKey);
    }
}