package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Activity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class ActivityFormDTO {

    Long id;
    String name;
    String description;
    MultipartFile mainImg;
    MultipartFile contentImg;
    Boolean shownInAd;
    Date activityStartDate;
    Date activityEndDate;


    public Activity toActivity() {
        return new Activity(id, name, description, shownInAd,
                activityStartDate, activityEndDate, mainImg, contentImg);
    }

}
