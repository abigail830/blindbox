package com.github.tuding.blindbox.domain;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Activity {

    String id;
    String activityName;
    String activityDescription;
    MultipartFile mainImg;
    Boolean shownInAd;
    Timestamp activityStartDate;
    Timestamp activityEndDate;

    Timestamp lastUpdateTime;
    String mainImgAddr;

    public Activity(String id, String name, String description, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate, MultipartFile mainImg, String mainImgAddr) {
        if (!Strings.isNullOrEmpty(id)) this.id = id;
        this.activityName = name;
        this.activityDescription = description;
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

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", shownInAd=" + shownInAd +
                ", activityStartDate=" + activityStartDate +
                ", activityEndDate=" + activityEndDate +
                ", lastUpdateTime=" + lastUpdateTime +
                ", mainImgAddr='" + mainImgAddr + '\'' +
                '}';
    }
}
