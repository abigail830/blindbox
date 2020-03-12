package com.github.tuding.blindbox.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class SeriesDTO {
    Long id;
    String name;
    Date releaseDate;
    boolean isNewSeries;
    //image b
    String seriesImage;
    //image d
    String matrixHeaderImage;
    //image e
    String matrixCellImage;
    BigDecimal price;

}
