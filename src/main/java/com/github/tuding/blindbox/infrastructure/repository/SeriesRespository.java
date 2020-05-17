package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class SeriesRespository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private RolesRepository rolesRepository;

    private RowMapper<Series> rowMapper = new BeanPropertyRowMapper<>(Series.class);

    @Deprecated
    public void saveSeries(Series series) {
        log.info("handle series creation as {}", series);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO series_tbl " +
                    " (id, roleID, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, " +
                    " matrixCellImage, columnSize, longImage, boxImage) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                    series.getLongImage(),
                    series.getBoxImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO series_tbl " +
                    " (id, roleID, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, " +
                    " matrixCellImage, columnSize, longImage, boxImage) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                    series.getLongImage(),
                    series.getBoxImage());
            log.info("update row {} ", update);
        }
    }


    @Deprecated
    public Optional<Series> querySeriesByName(String name) {
        log.info("Going to query series with name: {}", name);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_tbl WHERE name = ?", rowMapper, name);
        return seriesList.stream().findFirst();
    }

    @Deprecated
    public Optional<Series> querySeriesByID(String id) {
        log.info("Going to query series with id: {}", id);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_tbl WHERE id = ?", rowMapper, id);
        return seriesList.stream().findFirst();
    }

    @Deprecated
    public List<Series> queryByRoleID(String roleID) {
        log.info("Going to query series with role id: {}", roleID);
        return jdbcTemplate.query("SELECT * FROM series_tbl WHERE roleId = ?", rowMapper, roleID);
    }

    @Deprecated
    public List<Series> queryRoles() {
        log.info("Going to query series ");
        return jdbcTemplate.query("SELECT * FROM series_tbl", rowMapper);
    }

    @Deprecated
    public void createSeries(Series series) {
        Optional<Role> role = rolesRepository.queryRolesByID(series.getRoleId());
        if (role.isPresent()) {
            saveSeries(series);
        } else {
            throw new RuntimeException("Role id " + series.getRoleId() + " is not existed");
        }
    }

    @Deprecated
    public void deleteSeries(String id) {
        log.info("Delete series for {}", id);
        jdbcTemplate.update("DELETE FROM series_tbl where id = ?", id);
    }

    @Deprecated
    public void updateSeries(Series series) {
        if (StringUtils.isNotBlank(series.getReleaseDate())) {
            String updateSql = "UPDATE series_tbl " +
                    " SET name = ?, releaseDate = ?, isNewSeries = ?, isPresale = ?, price = ?, columnSize = ?, boxImage = ?" +
                    " WHERE id = ? ";
            int update = jdbcTemplate.update(updateSql,
                    series.getName(),
                    series.getReleaseDate(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getColumnSize(),
                    series.getBoxImage(),
                    series.getId());
            log.info("update row {} in series_tbl with ReleaseDate", update);
        } else {
            String updateSql = "UPDATE series_tbl " +
                    " SET name = ?, isNewSeries = ?, isPresale = ?, price = ?, columnSize = ?, boxImage = ?" +
                    " WHERE id = ? ";
            int update = jdbcTemplate.update(updateSql,
                    series.getName(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getColumnSize(),
                    series.getBoxImage(),
                    series.getId());
            log.info("update row {} in series_tbl without ReleaseDate", update);
        }

    }

    @Deprecated
    public List<Series> queryAllNewSeries() {
        log.info("Going to query all new series");
        return jdbcTemplate.query("SELECT * FROM series_tbl where isNewSeries = true", rowMapper);
    }

    @Deprecated
    public List<Series> queryAllSeriesWithPaging(Integer limitPerPage, Integer numOfPage) {
        log.info("Going to query all series");
        return jdbcTemplate.query("SELECT * FROM series_tbl LIMIT ? OFFSET ?", rowMapper, limitPerPage, numOfPage);
    }

    public List<Series> queryAllSeries() {
        log.info("Going to query all series.");
        return jdbcTemplate.query("SELECT * FROM series_v2_tbl", rowMapper);
    }

    public Optional<Series> querySeriesV2ByID(String id) {
        log.info("Going to query series with id: {}", id);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_v2_tbl WHERE id = ?", rowMapper, id);
        return seriesList.stream().findFirst();
    }

    public List<String> queryLinkedRoleIdsBySeriesId(String seriesId) {
        String sql = "select roleId from series_role_mapping_tbl where seriesId = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{seriesId}, String.class);
    }

    public void createSeriesV2(Series series) {
        log.info("handle series creation as {}", series);
        if (Toggle.TEST_MODE.isON()) {
            insertSeriesTbl(series, "INSERT INTO series_v2_tbl ");
        } else {
            insertSeriesTbl(series, "INSERT ignore INTO series_v2_tbl ");
        }
    }

    public void addSeriesRoleMappingV2(String seriesId, List<String> roleIds) {
        log.info("handle series {} mapping with roles {}", seriesId, roleIds);
        if (Toggle.TEST_MODE.isON()) {
            insertSeriesRoleMapping(seriesId, roleIds, "INSERT INTO series_role_mapping_tbl ");
        } else {
            insertSeriesRoleMapping(seriesId, roleIds, "INSERT ignore INTO series_role_mapping_tbl ");
        }
    }

    private void insertSeriesRoleMapping(String seriesId, List<String> roleIds, String header) {
        String insertMappingSql = header + " (seriesId, roleId) VALUES (?, ?)";
        for (String id : roleIds) {
            int updateMapping = jdbcTemplate.update(insertMappingSql,
                    seriesId,
                    id);
            log.info("update row {} in series_role_mapping_tbl ", updateMapping);
        }
    }

    public int removeSeriesRoleMapping(String seriesId, List<String> roleIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("roleIds", roleIds);
        parameters.addValue("seriesId", seriesId);

        String sql = "DELETE from series_role_mapping_tbl where seriesId = (:seriesId) and roleId in (:roleIds)";
        log.info("Going to remove row {} - {} in series_role_mapping_tbl ", seriesId, roleIds);
        return namedParameterJdbcTemplate.update(sql, parameters);

    }

    private void insertSeriesTbl(Series series, String header) {
        String insertSql = header +
                " (id, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, " +
                " matrixCellImage, columnSize, totalSize, longImage, boxImage, posterBgImage) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int update = jdbcTemplate.update(insertSql,
                series.getId(),
                series.getName(),
                series.getReleaseDate(),
                series.getIsNewSeries(),
                series.getIsPresale(),
                series.getPrice(),
                series.getSeriesImage(),
                series.getMatrixHeaderImage(),
                series.getMatrixCellImage(),
                series.getColumnSize(),
                series.getTotalSize(),
                series.getLongImage(),
                series.getBoxImage(),
                series.getPosterBgImage());
        log.info("update row {} ", update);
    }

    public void updateSeriesV2(Series series) {
        if (StringUtils.isNotBlank(series.getReleaseDate())) {
            String updateSql = "UPDATE series_v2_tbl " +
                    " SET name = ?, releaseDate = ?, isNewSeries = ?, isPresale = ?, price = ?, columnSize = ?," +
                    " totalSize = ?, boxImage = ?" +
                    " WHERE id = ? ";
            int update = jdbcTemplate.update(updateSql,
                    series.getName(),
                    series.getReleaseDate(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getColumnSize(),
                    series.getTotalSize(),
                    series.getBoxImage(),
                    series.getId());
            log.info("update row {} ", update);
        } else {
            String updateSql = "UPDATE series_v2_tbl " +
                    " SET name = ?, isNewSeries = ?, isPresale = ?, price = ?, columnSize = ?, totalSize = ?, boxImage = ?" +
                    " WHERE id = ? ";
            int update = jdbcTemplate.update(updateSql,
                    series.getName(),
                    series.getIsNewSeries(),
                    series.getIsPresale(),
                    series.getPrice(),
                    series.getColumnSize(),
                    series.getTotalSize(),
                    series.getBoxImage(),
                    series.getId());
            log.info("update row {} ", update);
        }

    }
}
