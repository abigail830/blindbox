package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Setter
@Getter
public class SeriesV2DTO {
    String id;
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
    Integer totalSize;

    String longImage;
    MultipartFile longImageFile;

    String boxImage;
    MultipartFile boxImageFile;

    String posterBgImage;
    MultipartFile posterBgImageFile;

    List<String> orilinkedRoleIds = new ArrayList<>();
    List<String> newlinkedRoleIds = new ArrayList<>();

    public SeriesV2DTO(Series series) {
        this.id = series.getId();
        this.name = series.getName();
        this.releaseDate = series.getReleaseDate();
        this.isNewSeries = series.getIsNewSeries();
        this.isPresale = series.getIsPresale();
        this.price = series.getPrice();
        this.columnSize = series.getColumnSize();
        this.totalSize = series.getTotalSize();
        this.seriesImage = Constant.ADMIN_UI_IMAGE_PATH + series.getSeriesImage();
        this.matrixHeaderImage = Constant.ADMIN_UI_IMAGE_PATH + series.getMatrixHeaderImage();
        this.matrixCellImage = Constant.ADMIN_UI_IMAGE_PATH + series.getMatrixCellImage();
        this.longImage = Constant.ADMIN_UI_IMAGE_PATH + series.getLongImage();
        this.boxImage = Constant.ADMIN_UI_IMAGE_PATH + series.getBoxImage();
        this.posterBgImage = Constant.ADMIN_UI_IMAGE_PATH + series.getPosterBgImage();
        this.orilinkedRoleIds = series.getLinkedRoleIds();
        this.newlinkedRoleIds = series.getLinkedRoleIds();
    }

    public Series toDomainObject() {
        Series series = new Series();
        series.setId(id);
        series.setName(name);
        series.setReleaseDate(releaseDate);
        series.setIsNewSeries(isNewSeries);
        series.setPrice(price);
        series.setIsPresale(isPresale);
        series.setSeriesImage(seriesImage);
        series.setMatrixHeaderImage(matrixHeaderImage);
        series.setMatrixCellImage(matrixCellImage);
        series.setColumnSize(columnSize);
        series.setTotalSize(totalSize);
        series.setLongImage(longImage);
        series.setBoxImage(boxImage);
        series.setPosterBgImage(posterBgImage);
        series.setLinkedRoleIds(orilinkedRoleIds);
        return series;
    }

    public Boolean isContainedRole(String roleId) {
        if (orilinkedRoleIds != null && !orilinkedRoleIds.isEmpty())
            return orilinkedRoleIds.stream().anyMatch(roleId::equals);
        else
            return false;
    }

    public void setRoleIds(List<String> linkedRoleIds) {
        this.orilinkedRoleIds = linkedRoleIds;
        this.newlinkedRoleIds = linkedRoleIds;
    }
}
