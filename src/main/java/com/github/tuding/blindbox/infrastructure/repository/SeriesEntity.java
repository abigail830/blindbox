package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Series;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SeriesEntity extends Series {
    String roleId;

    public Series toSeries() {
        return new Series(this.getId(), this.getName(), this.getReleaseDate(), this.getIsNewSeries(), this.getIsPresale(),
                this.getPrice(), this.getSeriesImage(), this.getMatrixHeaderImage(), this.getMatrixCellImage(),
                this.getColumnSize(), this.getTotalSize(), this.getLongImage(), this.getBoxImage(), this.getPosterBgImage());
    }

    @Override
    public String toString() {
        return "Series{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", releaseDate='" + getReleaseDate() + '\'' +
                ", isNewSeries=" + getIsNewSeries() +
                ", isPresale=" + getIsPresale() +
                ", price=" + getPrice() +
                ", seriesImage='" + getSeriesImage() + '\'' +
                ", matrixHeaderImage='" + getMatrixHeaderImage() + '\'' +
                ", matrixCellImage='" + getMatrixCellImage() + '\'' +
                ", columnSize=" + getColumnSize() +
                ", totalSize=" + getTotalSize() +
                ", longImage='" + getLongImage() + '\'' +
                ", boxImage='" + getBoxImage() + '\'' +
                ", posterBgImage='" + getPosterBgImage() + '\'' +
                ", linkedRoleIds=" + getLinkedRoleIds() + '\'' +
                ", roleId=" + getRoleId() +
                '}';
    }
}
