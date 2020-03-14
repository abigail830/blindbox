package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
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
    @DataSet("test-data/empty-series.yml")
    @ExpectedDataSet("expect-data/save-series.yml")
    void saveSeries1() {
        Toggle.TEST_MODE.setStatus(true);
        seriesRespository.saveSeries(new SeriesDTO(1L, 1L,
                "testSeries", new Date(), false, false, BigDecimal.valueOf(25.5),
                "/app/data/series/testSeries.png", "/app/data/series/header/testSeries.png",
                "/app/data/series/cell/testSeries.png"));
    }


    @Test
    @DataSet("test-data/series-data.yml")
    void getSeriesByID() {
        final Optional<SeriesDTO> result = seriesRespository.querySeriesByName("testSeries2");
        assertTrue(result.isPresent());
    }

    @Test
    @DataSet("test-data/series-data.yml")
    void getSeries() {
        final List<SeriesDTO> result = seriesRespository.queryByRoleID(1L);
        assertThat(result.size(), is(2));
    }
}