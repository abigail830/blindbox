package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private void saveDraw(Draw draw) {
        log.info("Going to insert draw_tbl for {}", draw);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO draw_tbl (openId, drawId, drawStatus, productId, seriesId) " +
                    "VALUES (?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, draw.getDrawId(), draw.getDrawId(), draw.getDrawStatus(),
                    draw.getProductId(), draw.getSeriesId());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO draw_tbl (openId, drawId, drawStatus, productId, seriesId) " +
                    "VALUES (?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, draw.getDrawId(), draw.getDrawId(), draw.getDrawStatus(),
                    draw.getProductId(), draw.getSeriesId());
            log.info("update row {} ", update);
        }
    }

    public List<Draw> getDraws() {
        log.info("Going to query draws ");
        return jdbcTemplate.query("SELECT * FROM draw_tbl", rowMapper);
    }


}
