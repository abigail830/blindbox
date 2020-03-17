package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Activity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ActivityFormDTO {

    String id;
    String name;
    String description;
    MultipartFile mainImg;
    MultipartFile contentImg;
    Boolean shownInAd;
    Date activityStartDate;
    Date activityEndDate;

    String mainImgAddr;
    String contentImgAddr;

    Boolean readOnly;
    String title;

    public ActivityFormDTO(Mode mode) {
        setupMode(mode);
    }

    public ActivityFormDTO(Activity activity, Mode mode) {
        this.id = activity.getId();
        this.name = activity.getActivityName();
        this.description = activity.getActivityDescription();
        this.shownInAd = activity.getShownInAd();
        this.mainImgAddr = activity.getMainImgAddr();
        this.contentImgAddr = activity.getContentImgAddr();

        if (activity.getActivityStartDate() != null)
            this.activityStartDate = new Date(activity.getActivityStartDate().getTime());

        if (activity.getActivityEndDate() != null)
            this.activityEndDate = new Date(activity.getActivityEndDate().getTime());

        setupMode(mode);

    }

    public Activity toActivity() {
        return new Activity(id, name, description, shownInAd,
                activityStartDate, activityEndDate, mainImg, contentImg);
    }

    public void setupMode(Mode mode) {
        if (mode.equals(Mode.ADD)) {
            this.readOnly = Boolean.FALSE;
            this.title = "添加活动";
        } else if (mode.equals(Mode.EDIT)) {
            this.readOnly = Boolean.FALSE;
            this.title = "编辑活动";
        } else {
            this.readOnly = Boolean.TRUE;
            this.title = "查看活动";
        }
    }
}
