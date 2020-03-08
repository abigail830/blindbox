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
    private String gender;
    private String nickName;
    private String city;
    private String country;
    private String province;
    private String lang;
    private String avatarUrl;

    public User(String openId) {
        this.openId = openId;
    }

    public User(Long id, String openId) {
        this.id = id;
        this.openId = openId;
    }
}
