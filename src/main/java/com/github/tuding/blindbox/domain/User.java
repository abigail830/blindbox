package com.github.tuding.blindbox.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class User {

    private String id;
    private String status;

    // Jws info
    private String jwtToken;

    // Wechat info
    private String openId;
    private String gender;
    private String nickName;
    private String city;
    private String country;
    private String province;
    private String language;
    private String avatarUrl;

    public User(String openId) {
        this.openId = openId;
    }

    public void assignToken(String jwtToken) {
        this.jwtToken = jwtToken;
        this.status = UserStatus.LOGIN.name();
    }
}
