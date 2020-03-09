package com.github.tuding.blindbox.api.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ActivityBriefDTO {

    Long id;
    String name;
    String description;
    Boolean showAsAd;

    Date activityStartDate;
    Date activityEndDate;
    Date createDate;
    Date lastUpdateDate;

}
