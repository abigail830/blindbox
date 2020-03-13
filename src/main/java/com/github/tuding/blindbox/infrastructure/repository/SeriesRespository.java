package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.api.dto.SeriesDTO;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class SeriesRespository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<SeriesDTO> rowMapper = new BeanPropertyRowMapper<SeriesDTO>();

    public void saveSeries(SeriesDTO seriesDTO) {
        log.info("handle series creation as {}", seriesDTO);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO series_tbl " +
                    " (roleID, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, matrixCellImage) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql,
                    seriesDTO.getRoleId(),
                    seriesDTO.getName(),
                    seriesDTO.getReleaseDate(),
                    seriesDTO.isNewSeries(),
                    seriesDTO.isPresale(),
                    seriesDTO.getPrice(),
                    seriesDTO.getSeriesImage(),
                    seriesDTO.getMatrixHeaderImage(),
                    seriesDTO.getMatrixCellImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO series_tbl " +
                    " (roleID, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, matrixCellImage) " +
                    " VALUES (?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql,
                    seriesDTO.getRoleId(),
                    seriesDTO.getName(),
                    seriesDTO.getReleaseDate(),
                    seriesDTO.isNewSeries(),
                    seriesDTO.isPresale(),
                    seriesDTO.getPrice(),
                    seriesDTO.getSeriesImage(),
                    seriesDTO.getMatrixHeaderImage(),
                    seriesDTO.getMatrixCellImage());
            log.info("update row {} ", update);
        }
    }


    public Optional<SeriesDTO> querySeriesByName(String name) {
        log.info("Going to query series with name: {}", name);

        List<SeriesDTO> seriesDTOs = jdbcTemplate.query("SELECT * FROM series_tbl WHERE name = ?", rowMapper, name);
        return seriesDTOs.stream().findFirst();
    }

    public List<SeriesDTO> queryRoles() {
        log.info("Going to query series ");
        return jdbcTemplate.query("SELECT * FROM series_tbl", rowMapper);
    }

}
