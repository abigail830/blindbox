package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    MultipartFile seriesImageFile;

    //image d
    String matrixHeaderImage;
    MultipartFile matrixHeaderImageFile;

    //image e
    String matrixCellImage;
    MultipartFile matrixCellImageFile;

    Integer columnSize;

    String longImage;
    MultipartFile longImageFile;

    String boxImage;
    MultipartFile boxImageFile;

    public Series toDomainObject() {
        Series series = new Series();
        series.setId(id);
        series.setRoleId(roleId);
        series.setName(name);
        series.setReleaseDate(releaseDate);
        series.setIsNewSeries(isNewSeries);
        series.setPrice(price);
        series.setIsPresale(isPresale);
        series.setSeriesImage(seriesImage);
        series.setMatrixHeaderImage(matrixHeaderImage);
        series.setMatrixCellImage(matrixCellImage);
        series.setColumnSize(columnSize);
        series.setLongImage(longImage);
        series.setBoxImage(boxImage);
        return series;
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
        this.seriesImage = Constant.ADMIN_UI_IMAGE_PATH + series.getSeriesImage();
        this.matrixHeaderImage = Constant.ADMIN_UI_IMAGE_PATH + series.getMatrixHeaderImage();
        this.matrixCellImage = Constant.ADMIN_UI_IMAGE_PATH + series.getMatrixCellImage();
        this.longImage = Constant.ADMIN_UI_IMAGE_PATH + series.getLongImage();
        this.boxImage = Constant.ADMIN_UI_IMAGE_PATH + series.getBoxImage();
    }
}
