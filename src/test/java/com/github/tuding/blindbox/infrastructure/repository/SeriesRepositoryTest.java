package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.product.Series;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DBRider
class SeriesRepositoryTest {

    @Autowired
    SeriesRespository seriesRespository;


    @Test
    @DataSet("expect-data/save-product.yml")
    void getSeriesByID() {
        final Optional<Series> result = seriesRespository.querySeriesByName("testSeries2");
        assertTrue(result.isPresent());
    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void getSeries() {
        final List<Series> result = seriesRespository.queryByRoleID("roleid1");
        assertThat(result.size(), is(2));
    }


}