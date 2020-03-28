package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Activity;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetailDTO {

    String id;
    String activityName;
    String activityDescription;
    Boolean shownInAd;
    String activityStartDate;
    String activityEndDate;
    String lastUpdateTime;
    String mainImgUrl;


    public ActivityDetailDTO(Activity activity) {
        this.id = activity.getId();
        this.activityName = activity.getActivityName();
        this.activityDescription = activity.getActivityDescription();
        this.shownInAd = activity.getShownInAd();
        this.activityStartDate = activity.getActivityStartDate().toLocalDateTime().toString();
        this.activityEndDate = activity.getActivityEndDate().toLocalDateTime().toString();
        this.lastUpdateTime = activity.getLastUpdateTime().toLocalDateTime().toString();
        this.mainImgUrl = Constant.WX_UI_IMAGE_PATH + activity.getMainImgAddr();
    }
}
