package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    String releaseDate;
    Boolean isNewSeries;
    Boolean isPresale;
    BigDecimal price;

    //image b
    String seriesImage;
    MultipartFile seriesImageFile;

    //image d
    String matrixHeaderImage;
    MultipartFile matrixHeaderImageFile;

    //image e
    String matrixCellImage;
    MultipartFile matrixCellImageFile;

}
