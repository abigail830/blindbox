package com.github.tuding.blindbox.api.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ActivityDetailDTO {

    Long id;
    String name;
    String description;
    String headerImage;
    List<String> contentImages;
    Boolean showInAd;

    Date activityStartDate;
    Date activityEndDate;
    Date createDate;
    Date lastUpdateDate;

}
