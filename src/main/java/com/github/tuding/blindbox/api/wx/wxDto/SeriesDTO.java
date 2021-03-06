package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.domain.product.Series;
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

    Integer productVolume;

    String boxImage;

    String posterBgImage;

    List<String> linkedRoleIds;

    public SeriesDTO(Series series, List<Product> productList) {
        this.id = series.getId();
        this.roleId = series.getRoleId();
        this.name = series.getName();
        this.releaseDate = series.getReleaseDate();
        this.isNewSeries = series.getIsNewSeries();
        this.isPresale = series.getIsPresale();
        this.price = series.getPrice();
        this.columnSize = series.getColumnSize();
        this.totalSize = series.getTotalSize();
        this.seriesImage = Constant.WX_UI_IMAGE_PATH + series.getSeriesImage();
        this.matrixHeaderImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixHeaderImage();
        this.matrixCellImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixCellImage();
        this.longImage = Constant.WX_UI_IMAGE_PATH + series.getLongImage();
        this.boxImage = Constant.WX_UI_IMAGE_PATH + series.getBoxImage();
        this.posterBgImage = Constant.WX_UI_IMAGE_PATH + series.getPosterBgImage();
        this.productVolume = productList.size();
        this.linkedRoleIds = series.getLinkedRoleIds();
    }

    public SeriesDTO (Series series) {
        this.id = series.getId();
        this.roleId = series.getId();
        this.name = series.getName();
        this.releaseDate = series.getReleaseDate();
        this.isNewSeries = series.getIsNewSeries();
        this.isPresale = series.getIsPresale();
        this.price = series.getPrice();
        this.columnSize = series.getColumnSize();
        this.totalSize = series.getTotalSize();
        this.seriesImage = Constant.WX_UI_IMAGE_PATH + series.getSeriesImage();
        this.matrixHeaderImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixHeaderImage();
        this.matrixCellImage = Constant.WX_UI_IMAGE_PATH + series.getMatrixCellImage();
        this.longImage = Constant.WX_UI_IMAGE_PATH + series.getLongImage();
        this.boxImage = Constant.WX_UI_IMAGE_PATH + series.getBoxImage();
        this.posterBgImage = Constant.WX_UI_IMAGE_PATH + series.getPosterBgImage();
        this.linkedRoleIds = series.getLinkedRoleIds();
    }
}
