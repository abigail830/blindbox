package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.activity.Activity;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBriefDTO {

    String id;
    String activityName;
    Boolean shownInAd;
    String activityStartDate;
    String activityEndDate;
    String mainImgUrl;
    String contentImgUrl;

    public ActivityBriefDTO(Activity activity) {
        this.id = activity.getId();
        this.activityName = activity.getActivityName();
        this.shownInAd = activity.getShownInAd();
        this.activityStartDate = activity.getActivityStartDate().toLocalDateTime().toString();
        this.activityEndDate = activity.getActivityEndDate().toLocalDateTime().toString();
        this.mainImgUrl = Constant.WX_UI_IMAGE_PATH + activity.getMainImgAddr();
        this.contentImgUrl = Constant.WX_UI_IMAGE_PATH + activity.getContentImgAddr();
    }
}
