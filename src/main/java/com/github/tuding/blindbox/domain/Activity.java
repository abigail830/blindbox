package com.github.tuding.blindbox.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class Activity {

    Long id;
    String name;
    String description;
    MultipartFile mainImg;
    MultipartFile contentImg;
    Boolean shownInAd;
    Date activityStartDate;
    Date activityEndDate;

    Date createDate;
    String mainImgAddr;
    String contentImgAddr;

    public Activity(Long id, String name, String description, Boolean shownInAd,
                    Date activityStartDate, Date activityEndDate, MultipartFile mainImg, MultipartFile contentImg) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shownInAd = shownInAd;
        this.activityStartDate = activityStartDate;
        this.activityEndDate = activityEndDate;
        this.mainImg = mainImg;
        this.contentImg = contentImg;
    }
}
