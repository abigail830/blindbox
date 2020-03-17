package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Activity;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ActivityBriefDTO {

    String id;
    String name;
    String description;
    Boolean showAsAd;

    Date activityStartDate;
    Date activityEndDate;
    Date lastUpdateDate;

    public ActivityBriefDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getActivityName();
        this.description = activity.getActivityDescription();
        this.showAsAd = activity.getShownInAd();

        if (activity.getActivityStartDate() != null)
            this.activityStartDate = new Date(activity.getActivityStartDate().getTime());

        if (activity.getActivityEndDate() != null)
            this.activityEndDate = new Date(activity.getActivityEndDate().getTime());

        this.lastUpdateDate = new Date(activity.getLastUpdateTime().getTime());
    }
}
