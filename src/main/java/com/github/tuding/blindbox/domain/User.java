package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
public class User {

    private static final int FIRST_LOGIN_BONUS = 5;
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
    private Timestamp lastLoginDate;
    private Integer bonus;

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

    public void adjustBonusAndLastLoginDate() {
        if (lastLoginDate == null) {
            bonus = Constant.FIRST_LOGIN_BONUS;
        } else {
            if (isFirstLoginToday()) {
                bonus = bonus + Constant.FIRST_LOGIN_BONUS;
            }
        }
        lastLoginDate = Timestamp.valueOf(LocalDateTime.now());
    }

    private Boolean isFirstLoginToday() {
        final LocalDate lastLoginDate = this.lastLoginDate.toLocalDateTime().toLocalDate();
        final LocalDate currentDate = LocalDate.now();

        return !lastLoginDate.equals(currentDate);
    }

}
