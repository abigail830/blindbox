package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    private void saveDraw(Draw draw) {
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
        jdbcTemplate.update("UPDATE draw_tbl SET drawStatus = 'CANCELLED' WHERE drawId = ?", draw.getDrawId());
    }

    public BigDecimal getPriceByDrawId(String drawId) {
        String sql = "select s.price from series_tbl s left join draw_tbl d on s.ID = d.seriesId where d.drawId = ?";
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
}
