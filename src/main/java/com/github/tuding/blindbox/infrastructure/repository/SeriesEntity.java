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
        return this;
    }
}
