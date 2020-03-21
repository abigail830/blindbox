package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Activity;
import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    String startDate;
    String endDate;

//    private static final ThreadLocal<SimpleDateFormat> dateFormat
//            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd kk:mm"));

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm");

    public ActivityBriefDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getActivityName();
        this.description = activity.getActivityDescription();
        this.showAsAd = activity.getShownInAd();

        if (activity.getActivityStartDate() != null) {
            this.activityStartDate = new Date(activity.getActivityStartDate().getTime());
            this.startDate = dateFormat.format(this.activityStartDate);
        }
        if (activity.getActivityEndDate() != null) {
            this.activityEndDate = new Date(activity.getActivityEndDate().getTime());
            this.endDate = dateFormat.format(this.activityEndDate);
        }
        this.lastUpdateDate = new Date(activity.getLastUpdateTime().getTime());
    }
}
