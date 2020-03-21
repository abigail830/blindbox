package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Series;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;

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

    public SeriesDTO (Series series) {
        this.id = series.getId();
        this.roleId = series.getRoleId();
        this.name = series.getName();
        this.releaseDate = series.getReleaseDate();
        this.isNewSeries = series.getIsNewSeries();
        this.isPresale = series.getIsPresale();
        this.price = series.getPrice();
        this.seriesImage = Constant.WX_UI_IMAGE_PATH + series.getSeriesImage();
        this.matrixHeaderImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixHeaderImage();
        this.matrixCellImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixCellImage();
    }
}
