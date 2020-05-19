package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class SeriesRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private RolesRepository rolesRepository;

    private RowMapper<Series> rowMapper = new BeanPropertyRowMapper<>(Series.class);
    private RowMapper<SeriesEntity> seriesEntityRowMapper = new BeanPropertyRowMapper<>(SeriesEntity.class);

    public Optional<Series> querySeriesByNameWithoutRoleId(String name) {
        log.info("Going to query series with name: {}", name);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_v2_tbl WHERE name = ?", rowMapper, name);
        return seriesList.stream().findFirst();
    }

    public Optional<Series> querySeriesByIDWithoutRoleIds(String id) {
        log.info("Going to query series with id: {}", id);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_v2_tbl WHERE id = ?", rowMapper, id);
        return seriesList.stream().findFirst();
    }

    @Deprecated
    public Optional<Series> querySeriesByID(String id) {
        log.info("Going to query series with id: {}", id);

        List<Series> seriesList = jdbcTemplate.query("SELECT * FROM series_tbl WHERE id = ?", rowMapper, id);
        return seriesList.stream().findFirst();
    }

    List<Series> toSeriesList(List<SeriesEntity> seriesEntities) {
        final Map<Series, List<String>> collect = seriesEntities.stream().collect(Collectors.groupingBy(
                SeriesEntity::toSeries,
                Collectors.mapping(SeriesEntity::getRoleId, Collectors.toList())));
        log.info("----- {}", collect);

        List<Series> series = new ArrayList<>();
        collect.keySet().forEach(c -> {
            c.setLinkedRoleIds(collect.get(c));
            log.info("----- {}", c);
        });
        return new ArrayList<>(collect.keySet());
    }

    @Deprecated
    public List<Series> queryByRoleIDOld(String roleID) {
        log.info("Going to query series with role id: {}", roleID);
        return jdbcTemplate.query("SELECT * FROM series_tbl WHERE roleId = ?", rowMapper, roleID);
    }

    public List<Series> queryByRoleID(String roleID) {
        log.info("Going to query series with role id: {}", roleID);

        String sql = "select s.*, m.roleId from series_v2_tbl s inner join series_role_mapping_tbl m" +
                " where s.ID = m.seriesId and m.seriesId in" +
                " (select seriesID from series_role_mapping_tbl where roleId = ?)";
        final List<SeriesEntity> entities = jdbcTemplate.query(sql, seriesEntityRowMapper, roleID);

        return toSeriesList(entities);
    }

    @Transactional
    public void deleteSeries(String id) {
        log.info("Delete series for {}", id);
        jdbcTemplate.update("DELETE FROM series_v2_tbl where id = ?", id);
        jdbcTemplate.update("DELETE FROM series_role_mapping_tbl where seriesId = ?", id);
    }

    @Deprecated
    public List<Series> queryAllNewSeriesOld() {
        log.info("Going to query all new series");
        return jdbcTemplate.query("SELECT * FROM series_tbl where isNewSeries = true", rowMapper);
    }

    public List<Series> queryAllNewSeries() {
        log.info("Going to query all new series");
        final List<SeriesEntity> entities = jdbcTemplate.query("SELECT s.*, m.roleId FROM series_v2_tbl s" +
                " inner join series_role_mapping_tbl m" +
                " where s.ID = m.seriesId and s.isNewSeries = true;", seriesEntityRowMapper);
        return toSeriesList(entities);
    }

    @Deprecated
    public List<Series> querySeriesOld() {
        log.info("Going to query all series ");
        return jdbcTemplate.query("SELECT * FROM series_tbl", rowMapper);
    }

    @Deprecated
    public List<Series> queryAllSeriesWithPagingOld(Integer limitPerPage, Integer numOfPage) {
        log.info("Going to query all series");
        return jdbcTemplate.query("SELECT * FROM series_tbl LIMIT ? OFFSET ?", rowMapper, limitPerPage, numOfPage);
    }

    public List<Series> queryAllSeriesWithPaging(Integer limitPerPage, Integer numOfPage) {
        log.info("Going to query all series with paging");
        String sql = "SELECT s.*, m.roleId as roleId FROM series_v2_tbl s " +
                "inner join series_role_mapping_tbl m on s.ID = m.seriesId" +
                " where s.ID in (select * from ( select s.id from series_v2_tbl s limit ? offset ?) as t)";
        final List<SeriesEntity> entities = jdbcTemplate.query(sql, seriesEntityRowMapper, limitPerPage, numOfPage);
        return toSeriesList(entities);
    }

    public List<Series> queryAllSeriesWithoutRoleIds() {
        log.info("Going to query all series.");
        return jdbcTemplate.query("SELECT * FROM series_v2_tbl", rowMapper);
    }

    public Optional<Series> querySeriesByIDWithRoleIds(String id) {
        log.info("Going to query series with id: {}", id);
        String sql = "SELECT s.*, m.roleId FROM series_v2_tbl s" +
                " inner join series_role_mapping_tbl m" +
                " where s.ID = m.seriesId and s.ID = ?";
        final List<SeriesEntity> entities = jdbcTemplate.query(sql, seriesEntityRowMapper, id);
        return toSeriesList(entities).stream().findFirst();
    }

    public List<String> queryLinkedRoleIdsBySeriesId(String seriesId) {
        String sql = "select roleId from series_role_mapping_tbl where seriesId = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{seriesId}, String.class);
    }

    public int removeLinkedRolesWhenDelRole(String roleId) {
        String sql = "delete from series_role_mapping_tbl where roleId = ?";
        return jdbcTemplate.update(sql, roleId);
    }

    public void createSeriesV2(Series series) {
        log.info("handle series creation as {}", series);
        if (Toggle.TEST_MODE.isON()) {
            insertSeriesTbl(series, "INSERT INTO ");
        } else {
            insertSeriesTbl(series, "INSERT ignore INTO ");
        }
    }

    public void addSeriesRoleMappingV2(String seriesId, List<String> roleIds) {
        log.info("handle series {} mapping with roles {}", seriesId, roleIds);
        if (Toggle.TEST_MODE.isON()) {
            insertSeriesRoleMapping(seriesId, roleIds, "INSERT INTO ");
        } else {
            insertSeriesRoleMapping(seriesId, roleIds, "INSERT ignore INTO ");
        }
    }

    private void insertSeriesRoleMapping(String seriesId, List<String> roleIds, String header) {
        String insertMappingSql = header + "series_role_mapping_tbl (seriesId, roleId) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(insertMappingSql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, seriesId);
                ps.setString(2, roleIds.get(i));
            }

            public int getBatchSize() {
                return roleIds.size();
            }
        });
        log.info("insert row {} - {} in series_role_mapping_tbl ", seriesId, roleIds);
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
                "series_v2_tbl (id, name, releaseDate, isNewSeries, isPresale, price, seriesImage, matrixHeaderImage, " +
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
