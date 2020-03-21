package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Activity;
import lombok.*;

import java.sql.Timestamp;

import static com.github.tuding.blindbox.infrastructure.Constant.WX_UI_IMAGE_PATH;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    private static final String MAIN_IMG_URL = "/admin-ui/activities/id/UUID/mainImg";
    private static final String CONTENT_IMG_URL = "/admin-ui/activities/id/UUID/contentImg";
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
        this.mainImgUrl = WX_UI_IMAGE_PATH + activity.getMainImgAddr();
        this.contentImgUrl = WX_UI_IMAGE_PATH + activity.getContentImgAddr();
    }
}
