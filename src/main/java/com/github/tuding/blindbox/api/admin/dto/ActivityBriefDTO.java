package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Activity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Slf4j
public class ActivityBriefDTO {

    String id;
    String name;
    Boolean showAsAd;
    String lastUpdateDate;
    String startDate;
    String endDate;

    public ActivityBriefDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getActivityName();
        this.showAsAd = activity.getShownInAd();

        this.startDate = activity.getActivityStartDate().toLocalDateTime().toString();
        this.endDate = activity.getActivityEndDate().toLocalDateTime().toString();
        this.lastUpdateDate = activity.getActivityStartDate().toLocalDateTime().toString();
    }
}
