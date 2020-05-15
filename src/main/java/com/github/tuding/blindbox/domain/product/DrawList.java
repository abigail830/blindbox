package com.github.tuding.blindbox.domain.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DrawList {
    String openId;
    String drawListId;
    Map<Integer, Draw> drawGroup;
    String seriesId;
    Date drawTime;


    BigDecimal price;
    String boxImage;
    String seriesName;

}
