package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DrawRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    ProductRepository productRepository;

    private RowMapper<Draw> rowMapper = new BeanPropertyRowMapper<>(Draw.class);


    @Transactional
    public void persistADraw(Product product, Draw draw) {
        productRepository.updateProduct(product);
        saveDraw(draw);
    }

    @Transactional
    public void cancelADraw(Product product, Draw draw) {
        productRepository.updateProduct(product);
        cancelDraw(draw);
    }

    public List<Draw> getDraws() {
        log.info("Going to query draws ");
        return jdbcTemplate.query("SELECT * FROM draw_tbl", rowMapper);
    }


    public Optional<Draw> getDrawByDrawID(String drawId) {
        log.info("Going to query draws with id: {}", drawId);
        List<Draw> draws = jdbcTemplate.query("SELECT * FROM draw_tbl WHERE drawId = ?", rowMapper, drawId);
        return draws.stream().findFirst();
    }

    public Optional<Draw> getDrawByOpenID(String openID) {
        log.info("Going to query draws with openID: {}", openID);
        List<Draw> draws = jdbcTemplate.query("SELECT * FROM draw_tbl WHERE drawStatus = 'NEW' AND openId = ?", rowMapper, openID);
        return draws.stream().findFirst();
    }

    public void saveDraw(Draw draw) {
        log.info("Going to insert draw_tbl for {}", draw);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO draw_tbl (openId, drawId, drawStatus, productId, seriesId, boxImage, price, seriesName) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, draw.getOpenId(), draw.getDrawId(), draw.getDrawStatus(),
                    draw.getProductId(), draw.getSeriesId(), draw.getBoxImage(), draw.getPrice(), draw.getSeriesName());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO draw_tbl (openId, drawId, drawStatus, productId, seriesId, boxImage, price, seriesName) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, draw.getOpenId(), draw.getDrawId(), draw.getDrawStatus(),
                    draw.getProductId(), draw.getSeriesId(), draw.getBoxImage(), draw.getPrice(), draw.getSeriesName());
            log.info("update row {} ", update);
        }
    }


    private void cancelDraw(Draw draw) {
        jdbcTemplate.update("UPDATE draw_tbl SET drawStatus = ? WHERE drawId = ?",
                Constant.DRAW_CANCELLED_STATUS,
                draw.getDrawId());
    }

    public BigDecimal getPriceByDrawId(String drawId) {
        String sql = "select s.price from series_v2_tbl s left join draw_tbl d on s.ID = d.seriesId where d.drawId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{drawId}, BigDecimal.class);
        } catch (EmptyResultDataAccessException ex) {
            throw new BizException(ErrorCode.DRAWID_NOT_FOUND);
        } catch (Exception ex) {
            log.warn("{}", ex);
            throw new BizException(ErrorCode.FAIL_TO_GET_PRICE_BY_DRAWID);
        }
    }

    public void updateDrawPriceById(BigDecimal priceAfterDiscount, String drawId) {
        jdbcTemplate.update("UPDATE draw_tbl SET price = ? WHERE drawId = ?", priceAfterDiscount, drawId);
    }

    public void confirmDrawToOrder(String drawId) {
        jdbcTemplate.update("UPDATE draw_tbl SET drawStatus = ? WHERE drawId = ?", Constant.DRAW_ORDER_STATUS, drawId);

    }

    @Transactional
    public void saveDraw(List<Draw> draws) {
        List<String> productIDs = draws.stream().map(item -> item.getProductId()).collect(Collectors.toList());
        productRepository.reduceStock(productIDs);
        String insertSql = "INSERT INTO draw_tbl (openId, drawId, drawStatus, productId, seriesId, boxImage, price, seriesName) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, draws.get(i).getOpenId());
                ps.setString(2, draws.get(i).getDrawId());
                ps.setString(3, draws.get(i).getDrawStatus());
                ps.setString(4, draws.get(i).getProductId());
                ps.setString(5, draws.get(i).getSeriesId());
                ps.setString(6, draws.get(i).getBoxImage());
                ps.setBigDecimal(7, draws.get(i).getPrice());
                ps.setString(8, draws.get(i).getSeriesName());
            }

            public int getBatchSize() {
                return draws.size();
            }
        });

    }
}
