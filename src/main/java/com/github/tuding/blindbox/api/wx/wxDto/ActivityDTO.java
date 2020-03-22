package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Activity;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    String id;
    String activityName;
    String activityDescription;
    Boolean shownInAd;
    Timestamp activityStartDate;
    Timestamp activityEndDate;
    Timestamp lastUpdateTime;
    String mainImgUrl;
    String contentImgUrl;

    public ActivityDTO(Activity activity) {
        this.id = activity.getId();
        this.activityName = activity.getActivityName();
        this.activityDescription = activity.getActivityDescription();
        this.shownInAd = activity.getShownInAd();
        this.activityStartDate = activity.getActivityStartDate();
        this.activityEndDate = activity.getActivityEndDate();
        this.mainImgUrl = Constant.WX_UI_IMAGE_PATH + activity.getMainImgAddr();
        this.contentImgUrl = Constant.WX_UI_IMAGE_PATH + activity.getContentImgAddr();
    }
}
