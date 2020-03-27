package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.Role;
import com.github.tuding.blindbox.domain.Series;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private RolesRepository rolesRepository;

    private RowMapper<Series> rowMapper = new BeanPropertyRowMapper<>(Series.class);

    public void saveSeries(Series series) {
        log.info("handle series creation as {}", series);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO series_tbl " +
                    " (id, roleID, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, matrixCellImage, columnSize, longImage) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql,
                    series.getId(),
                    series.getRoleId(),
                    series.getName(),
                    series.getReleaseDate(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getSeriesImage(),
                    series.getMatrixHeaderImage(),
                    series.getMatrixCellImage(),
                    series.getColumnSize(),
                    series.getLongImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO series_tbl " +
                    " (id, roleID, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, matrixCellImage, columnSize, longImage) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql,
                    series.getId(),
                    series.getRoleId(),
                    series.getName(),
                    series.getReleaseDate(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getSeriesImage(),
                    series.getMatrixHeaderImage(),
                    series.getMatrixCellImage(),
                    series.getColumnSize(),
                    series.getLongImage());
            log.info("update row {} ", update);
        }
    }


    public Optional<Series> querySeriesByName(String name) {
        log.info("Going to query series with name: {}", name);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_tbl WHERE name = ?", rowMapper, name);
        return seriesList.stream().findFirst();
    }

    public Optional<Series> querySeriesByID(String id) {
        log.info("Going to query series with id: {}", id);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_tbl WHERE id = ?", rowMapper, id);
        return seriesList.stream().findFirst();
    }

    public List<Series> queryByRoleID(String roleID) {
        log.info("Going to query series with role id: {}", roleID);
        return jdbcTemplate.query("SELECT * FROM series_tbl WHERE roleId = ?", rowMapper, roleID);

    }

    public List<Series> queryRoles() {
        log.info("Going to query series ");
        return jdbcTemplate.query("SELECT * FROM series_tbl", rowMapper);
    }

    public void createSeries(Series series) {
        Optional<Role> role = rolesRepository.queryRolesByID(series.getRoleId());
        if (role.isPresent()) {
            saveSeries(series);
        } else {
            throw new RuntimeException("Role id " + series.getRoleId() + " is not existed");
        }
    }

    public void deleteSeries(String id) {
        log.info("Delete series for {}", id);
        jdbcTemplate.update("DELETE FROM series_tbl where id = ?", id);


    }

    public void updateSeries(Series series) {
        if (StringUtils.isNotBlank(series.getReleaseDate())) {
            String updateSql = "UPDATE series_tbl " +
                    " SET name = ?, releaseDate = ?, isNewSeries = ?, isPresale = ?, price = ?, columnSize = ?" +
                    " WHERE id = ? ";
            int update = jdbcTemplate.update(updateSql,
                    series.getName(),
                    series.getReleaseDate(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getColumnSize(),
                    series.getId());
            log.info("update row {} ", update);
        } else {
            String updateSql = "UPDATE series_tbl " +
                    " SET name = ?, isNewSeries = ?, isPresale = ?, price = ?, columnSize = ?" +
                    " WHERE id = ? ";
            int update = jdbcTemplate.update(updateSql,
                    series.getName(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getColumnSize(),
                    series.getId());
            log.info("update row {} ", update);
        }

    }

    public List<Series> queryAllNewSeries() {
        log.info("Going to query all new series");
        return jdbcTemplate.query("SELECT * FROM series_tbl where isNewSeries = true", rowMapper);
    }

    public List<Series> queryAllSeriesWithPaging(Integer limitPerPage, Integer numOfPage) {
        log.info("Going to query all series");
        return jdbcTemplate.query("SELECT * FROM series_tbl LIMIT ? OFFSET ?", rowMapper, limitPerPage, numOfPage);
    }
}
