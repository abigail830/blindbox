package com.github.tuding.blindbox.domain;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DBRider
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    WxClient wxClient;

    @Autowired
    Jwt jwt;

    @Test
    @DataSet("test-data/user-service-wxauth.yml")
    void wxAuth() {
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
        ReflectionTestUtils.setField(wxClient, "appId", appId);

        // when
        final String token = jwt.generateWxToken(new User("oGZUI0egBJY1zhBYw2KhdUfwVJJE", sessionKey));
        userService.wxAuth(token, encryptedData, iv);

        User user = userService.getUserByToken(token);
        //then
        System.out.println(user);
        assertEquals("oGZUI0egBJY1zhBYw2KhdUfwVJJE", user.getOpenId());
        assertEquals("Band", user.getNickName());
    }
}