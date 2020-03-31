package com.github.tuding.blindbox.domain.product;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Draw {
    String openId;
    String drawId;
    String drawStatus;
    String productId;
    String seriesId;
    Date drawTime;


}
