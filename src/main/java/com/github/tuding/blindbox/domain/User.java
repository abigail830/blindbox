package com.github.tuding.blindbox.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    private Long id;

    // Wechat info
    private String openId;
    private String sessionKey;
    private String gender;
    private String nickName;
    private String city;
    private String country;
    private String province;
    private String lang;
    private String avatarUrl;
    private String unionId;

    public User(String openId, String sessionKey) {
        this.openId = openId;
        this.sessionKey = sessionKey;
    }


    public User(String openId, String gender, String nickName,
                String city, String country, String province, String avatarUrl, String unionId) {
        this.openId = openId;
        this.gender = gender;
        this.nickName = nickName;
        this.city = city;
        this.country = country;
        this.province = province;
        this.avatarUrl = avatarUrl;
        this.unionId = unionId;
    }
}
