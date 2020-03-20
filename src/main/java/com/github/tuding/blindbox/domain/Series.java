package com.github.tuding.blindbox.domain;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Series {

    String id;
    String roleId;
    String name;
    String releaseDate;
    Boolean isNewSeries;
    Boolean isPresale;
    BigDecimal price;

    //image b
    String seriesImage;

    //image d
    String matrixHeaderImage;

    //image e
    String matrixCellImage;
}
