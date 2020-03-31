package com.github.tuding.blindbox.infrastructure.client;

import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WXBizDataCryptTest {

    @Test
    void decryptData() {
        String appId = "wx4f4bc4dec97d474b";
        String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
        String encryptedData = "CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZM"
                + "QmRzooG2xrDcvSnxIMXFufNstNGTyaGS"
                + "9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+"
                + "3hVbJSRgv+4lGOETKUQz6OYStslQ142d"
                + "NCuabNPGBzlooOmB231qMM85d2/fV6Ch"
                + "evvXvQP8Hkue1poOFtnEtpyxVLW1zAo6"
                + "/1Xx1COxFvrc2d7UL/lmHInNlxuacJXw"
                + "u0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn"
                + "/Hz7saL8xz+W//FRAUid1OksQaQx4CMs"
                + "8LOddcQhULW4ucetDf96JcR3g0gfRK4P"
                + "C7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB"
                + "6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns"
                + "/8wR2SiRS7MNACwTyrGvt9ts8p12PKFd"
                + "lqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYV"
                + "oKlaRv85IfVunYzO0IKXsyl7JCUjCpoG"
                + "20f0a04COwfneQAGGwd5oa+T8yO5hzuy" + "Db/XcxxmK01EpqOyuxINew==";
        String iv = "r7BXXKkLb8qrSNn05n0qiA==";

        WXBizDataCrypt biz = new WXBizDataCrypt(appId, sessionKey);

        final String response = biz.decryptData(encryptedData, iv);
        final WxDecryptResponse result = JsonUtil.toObject(response, WxDecryptResponse.class);
        System.out.println(result);

        assertEquals("oGZUI0egBJY1zhBYw2KhdUfwVJJE", result.getOpenId());
        assertEquals("Band", result.getNickName());
    }
}