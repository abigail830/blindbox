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
public class ActivityBriefDTO {

    String id;
    String activityName;
    Boolean shownInAd;
    Timestamp activityStartDate;
    Timestamp activityEndDate;
    String mainImgUrl;

    public ActivityBriefDTO(Activity activity) {
        this.id = activity.getId();
        this.activityName = activity.getActivityName();
        this.shownInAd = activity.getShownInAd();
        this.activityStartDate = activity.getActivityStartDate();
        this.activityEndDate = activity.getActivityEndDate();
        this.mainImgUrl = Constant.WX_UI_IMAGE_PATH + activity.getMainImgAddr();
    }
}
