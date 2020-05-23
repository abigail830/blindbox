package com.github.tuding.blindbox.domain.activity;


import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Activity {

    public static final String NOTIFY_SEPARATOR = "|";

    String id;
    String activityName;
    String activityDescription;
    MultipartFile mainImg;
    MultipartFile contentImg;
    Boolean shownInAd;
    Timestamp activityStartDate;
    Timestamp activityEndDate;

    Timestamp lastUpdateTime;
    String mainImgAddr;
    String contentImgAddr;

    String notify;
    String notifyJumpPage;

    Integer giftBonus;

    public Activity(String id, String name, String description, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate,
                    MultipartFile mainImg, String mainImgAddr,
                    MultipartFile contentImg, String contentImgAddr, Integer giftBonus) {
        this.id = Strings.isNullOrEmpty(id) ? null : id;
        this.activityName = Strings.isNullOrEmpty(name) ? null : name;
        this.activityDescription = Strings.isNullOrEmpty(description) ? "" : description;
        this.shownInAd = shownInAd;
        this.activityStartDate = new Timestamp(activityStartDate.getTime());
        this.activityEndDate = new Timestamp(activityEndDate.getTime());
        this.mainImg = mainImg;
        this.mainImgAddr = mainImgAddr;
        this.contentImg = contentImg;
        this.contentImgAddr = contentImgAddr;
        this.giftBonus = giftBonus;
    }

    public Activity(String id, String activityName, String activityDescription, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate, String mainImgAddr, String contentImgAddr,
                    Integer giftBonus) {
        this.id = id;
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.shownInAd = shownInAd;
        this.activityStartDate = new Timestamp(activityStartDate.getTime());
        this.activityEndDate = new Timestamp(activityEndDate.getTime());
        this.mainImgAddr = mainImgAddr;
        this.contentImgAddr = contentImgAddr;
        this.giftBonus = giftBonus;
    }

    public void addNotifyInfo(String openId, String redirectPage) {
        updateNotifier(openId);
        updateRedirectPage(redirectPage);
    }

    private void updateRedirectPage(String redirectPage) {
        if (Strings.isNullOrEmpty(redirectPage)) {
            throw new BizException(ErrorCode.NOTIFY_REDIRECT_PAGE_SHOULD_NOT_BE_NULL_OR_EMPTY);
        } else {
            this.notifyJumpPage = redirectPage;
        }
    }

    private void updateNotifier(String openId) {
        if (!isFutureActivity()) {
            throw new BizException(ErrorCode.SHOULD_NOT_REG_NOTIFY_FOR_ACTIVITY_WHEN_STARTED);
        }

        if (Strings.isNullOrEmpty(this.notify)) {
            this.notify = openId;
        } else {
            Set<String> notifySet = getNotifierAsSet();
            notifySet.add(openId);
            setNotify(notifySet);
        }
    }


    public Set<String> getNotifierAsSet() {
        if (Strings.isNullOrEmpty(this.notify)) {
            return new HashSet<>();
        } else {
            final String[] splits = this.notify.split("\\" + Activity.NOTIFY_SEPARATOR, -2);
            return new HashSet<>(Arrays.asList(splits));
        }
    }

    public Boolean isExistingSubscriber(String openId) {
        if (Strings.isNullOrEmpty(this.notify))
            return Boolean.FALSE;
        else
            return this.notify.contains(openId);
    }

    public void setNotify(Set<String> notifySet) {
        if (null != notifySet && !notifySet.isEmpty()) {
            this.notify = String.join(Activity.NOTIFY_SEPARATOR, notifySet);
        }
    }

    public void setNotify(String notifier) {
        if (!Strings.isNullOrEmpty(notifier)) {
            this.notify = notifier;
        }
    }

    public Boolean isFutureActivity() {
        return activityStartDate.toLocalDateTime().isAfter(LocalDateTime.now());
    }

    public Boolean isExpiriedActivity() {
        return activityEndDate.toLocalDateTime().isBefore(LocalDateTime.now());
    }
}
