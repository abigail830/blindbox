package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Product;
import com.github.tuding.blindbox.domain.Series;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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

    //image d
    String matrixHeaderImage;

    //image e
    String matrixCellImage;

    Integer columnSize;
    String longImage;

    Integer productVolume;

    public SeriesDTO (Series series, List<Product> productList) {
        this.id = series.getId();
        this.roleId = series.getRoleId();
        this.name = series.getName();
        this.releaseDate = series.getReleaseDate();
        this.isNewSeries = series.getIsNewSeries();
        this.isPresale = series.getIsPresale();
        this.price = series.getPrice();
        this.columnSize = series.getColumnSize();
        this.seriesImage = Constant.WX_UI_IMAGE_PATH + series.getSeriesImage();
        this.matrixHeaderImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixHeaderImage();
        this.matrixCellImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixCellImage();
        this.longImage = Constant.WX_UI_IMAGE_PATH + series.getLongImage();
        this.productVolume = productList.size();
    }

    public SeriesDTO (Series series) {
        this.id = series.getId();
        this.roleId = series.getRoleId();
        this.name = series.getName();
        this.releaseDate = series.getReleaseDate();
        this.isNewSeries = series.getIsNewSeries();
        this.isPresale = series.getIsPresale();
        this.price = series.getPrice();
        this.columnSize = series.getColumnSize();
        this.seriesImage = Constant.WX_UI_IMAGE_PATH + series.getSeriesImage();
        this.matrixHeaderImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixHeaderImage();
        this.matrixCellImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixCellImage();
        this.longImage = Constant.WX_UI_IMAGE_PATH + series.getLongImage();
    }
}
