package com.github.tuding.blindbox.domain;

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
    MultipartFile contentImg;
    Boolean shownInAd;
    Timestamp activityStartDate;
    Timestamp activityEndDate;

    Date createTime;
    String mainImgAddr;
    String contentImgAddr;

    public Activity(String id, String name, String description, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate, MultipartFile mainImg, MultipartFile contentImg) {
        this.id = id;
        this.activityName = name;
        this.activityDescription = description;
        this.shownInAd = shownInAd;
        this.activityStartDate = new Timestamp(activityStartDate.getTime());
        this.activityEndDate = new Timestamp(activityEndDate.getTime());
        this.mainImg = mainImg;
        this.contentImg = contentImg;
    }

    public Activity(String id, String activityName, String activityDescription, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate, String mainImgAddr, String contentImgAddr) {
        this.id = id;
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.shownInAd = shownInAd;
        this.activityStartDate = new Timestamp(activityStartDate.getTime());
        this.activityEndDate = new Timestamp(activityEndDate.getTime());
        this.mainImgAddr = mainImgAddr;
        this.contentImgAddr = contentImgAddr;
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
                ", createTime=" + createTime +
                ", mainImgAddr='" + mainImgAddr + '\'' +
                ", contentImgAddr='" + contentImgAddr + '\'' +
                '}';
    }
}
