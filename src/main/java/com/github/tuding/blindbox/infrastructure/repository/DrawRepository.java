package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.Draw;
import com.github.tuding.blindbox.domain.Product;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class DrawRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    ProductRepository productRepository;

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


}
