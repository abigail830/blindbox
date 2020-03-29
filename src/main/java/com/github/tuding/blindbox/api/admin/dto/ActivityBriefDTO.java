package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Activity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ActivityBriefDTO {

    String id;
    String name;
    Boolean showAsAd;
    String lastUpdateDate;
    String startDate;
    String endDate;
    Integer subscribeCount;

    public ActivityBriefDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getActivityName();
        this.showAsAd = activity.getShownInAd();
        this.startDate = activity.getActivityStartDate().toLocalDateTime().toString();
        this.endDate = activity.getActivityEndDate().toLocalDateTime().toString();
        this.subscribeCount = activity.getNotifierAsSet().size();
    }
}
