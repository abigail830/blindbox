package com.github.tuding.blindbox.domain;


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
    Boolean shownInAd;
    Timestamp activityStartDate;
    Timestamp activityEndDate;

    Timestamp lastUpdateTime;
    String mainImgAddr;

    String notify;


    public Activity(String id, String name, String description, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate, MultipartFile mainImg, String mainImgAddr) {
        this.id = Strings.isNullOrEmpty(id) ? null : id;
        this.activityName = Strings.isNullOrEmpty(name) ? null : name;
        this.activityDescription = Strings.isNullOrEmpty(description) ? "" : description;
        this.shownInAd = shownInAd;
        if (activityStartDate != null) {
            this.activityStartDate = new Timestamp(activityStartDate.getTime());
        } else {
            this.activityStartDate = new Timestamp(new Date().getTime());
        }
        if (activityEndDate != null) {
            this.activityEndDate = new Timestamp(activityEndDate.getTime());
        } else {
            this.activityEndDate = new Timestamp(new Date().getTime());
        }
        this.mainImg = mainImg;
        this.mainImgAddr = mainImgAddr;
    }

    public Activity(String id, String activityName, String activityDescription, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate, String mainImgAddr) {
        this.id = id;
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.shownInAd = shownInAd;
        this.activityStartDate = new Timestamp(activityStartDate.getTime());
        this.activityEndDate = new Timestamp(activityEndDate.getTime());
        this.mainImgAddr = mainImgAddr;
    }

    public void addNotifier(String openId) {

        if (activityStartDate.toLocalDateTime().isBefore(LocalDateTime.now())) {
            throw new BizException(ErrorCode.ACTIVITY_ALREADY_PASSED);
        }

        if (Strings.isNullOrEmpty(this.notify)) {
            this.notify = openId;
        } else {
            final String[] splits = this.notify.split(Activity.NOTIFY_SEPARATOR, -2);
            Set<String> notifySet = new HashSet<>(Arrays.asList(splits));
            notifySet.add(openId);
            this.notify = String.join(Activity.NOTIFY_SEPARATOR, notifySet);
        }
    }

}
