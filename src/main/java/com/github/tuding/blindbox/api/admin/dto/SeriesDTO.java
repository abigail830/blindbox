package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class SeriesDTO {
    String id;
    String roleId;
    String name;
    Date releaseDate;
    boolean isNewSeries;
    boolean isPresale;
    BigDecimal price;

    //image b
    String seriesImage;
    //image d
    String matrixHeaderImage;
    //image e
    String matrixCellImage;

}
