package com.github.tuding.blindbox.domain.user;

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
    private Timestamp lastShareCollectionDate;
    private Timestamp lastShareActivityDate;
    private Integer bonus;

    public User(String openId, String sessionKey) {
        this.openId = openId;
        this.sessionKey = sessionKey;
        this.bonus = 0;
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

    public void updateLoginBonus() {
        if (isFirstLoginToday()) {
            bonus += Constant.FIRST_LOGIN_BONUS;
        }
        lastLoginDate = Timestamp.valueOf(LocalDateTime.now());
    }

    public void shareCollection() {
        if (isFirstShareCollectionToday()) {
            bonus += Constant.SHARE_COLLECTION;
        }
        lastShareCollectionDate = Timestamp.valueOf(LocalDateTime.now());
    }

    public void lightUpCollection() {
        bonus += Constant.LIGHT_UP_COLLECTION;
    }

    public void shareActivity() {
        if (isFirstShareActivityToday()) {
            bonus += Constant.SHARE_ACTIVITY;
        }
        lastShareActivityDate = Timestamp.valueOf(LocalDateTime.now());
    }

    private Boolean isFirstLoginToday() {
        return null == lastLoginDate || isToday(lastLoginDate);
    }

    private Boolean isFirstShareCollectionToday() {
        return null == lastShareCollectionDate || isToday(this.lastShareCollectionDate);
    }

    private Boolean isFirstShareActivityToday() {
        return null == lastShareActivityDate || isToday(this.lastShareActivityDate);
    }

    private Boolean isToday(Timestamp lastShareCollectionDate2) {
        final LocalDate lastShareCollectionDate = lastShareCollectionDate2.toLocalDateTime().toLocalDate();
        final LocalDate currentDate = LocalDate.now();

        return !lastShareCollectionDate.equals(currentDate);
    }


}
