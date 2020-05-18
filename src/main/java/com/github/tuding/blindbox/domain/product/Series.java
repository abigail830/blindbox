package com.github.tuding.blindbox.domain.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Series {

    String id;

    @Deprecated
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

    Integer columnSize;
    Integer totalSize;

    String longImage;

    String boxImage;

    String posterBgImage;

    List<String> linkedRoleIds;

    @Deprecated
    public Series(String id, String roleId, String name, String releaseDate, Boolean isNewSeries, Boolean isPresale,
                  BigDecimal price, String seriesImage, String matrixHeaderImage, String matrixCellImage,
                  Integer columnSize, Integer totalSize, String longImage, String boxImage, String posterBgImage) {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.releaseDate = releaseDate;
        this.isNewSeries = isNewSeries;
        this.isPresale = isPresale;
        this.price = price;
        this.seriesImage = seriesImage;
        this.matrixHeaderImage = matrixHeaderImage;
        this.matrixCellImage = matrixCellImage;
        this.columnSize = columnSize;
        this.totalSize = totalSize;
        this.longImage = longImage;
        this.boxImage = boxImage;
        this.posterBgImage = posterBgImage;
    }

    public Series(String id, String name, String releaseDate, Boolean isNewSeries, Boolean isPresale,
                  BigDecimal price, String seriesImage, String matrixHeaderImage, String matrixCellImage,
                  Integer columnSize, Integer totalSize, String longImage, String boxImage, String posterBgImage) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.isNewSeries = isNewSeries;
        this.isPresale = isPresale;
        this.price = price;
        this.seriesImage = seriesImage;
        this.matrixHeaderImage = matrixHeaderImage;
        this.matrixCellImage = matrixCellImage;
        this.columnSize = columnSize;
        this.totalSize = totalSize;
        this.longImage = longImage;
        this.boxImage = boxImage;
        this.posterBgImage = posterBgImage;
    }
}
