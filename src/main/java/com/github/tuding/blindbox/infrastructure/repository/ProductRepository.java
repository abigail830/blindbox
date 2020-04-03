package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Product;
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
public class ProductRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SeriesRespository seriesRespository;

    private RowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);

    private void saveProduct(Product product) {
        log.info("Going to insert product_v2_tbl for {}", product);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO product_v2_tbl (id, seriesID, name, isSpecial, stock, " +
                    " productImage, productGrayImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, product.getId(), product.getSeriesID(), product.getName(),
                    product.getIsSpecial(), product.getStock(),
                    product.getProductImage(), product.getProductGrayImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO product_v2_tbl (id, seriesID, name, isSpecial, stock, " +
                    " productImage, productGrayImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, product.getId(), product.getSeriesID(), product.getName(),
                    product.getIsSpecial(), product.getStock(),
                    product.getProductImage(), product.getProductGrayImage());
            log.info("update row {} ", update);
        }
    }

    public Optional<Product> getProductByID(String id) {
        log.info("Going to query product with id: {}", id);
        List<Product> products = jdbcTemplate.query("SELECT * FROM product_v2_tbl p " +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest " +
                " on p.id = latest.id and p.version = latest.mversion " +
                " WHERE p.id = ?", rowMapper, id);
        return products.stream().findFirst();
    }

    public List<Product> getProductBySeries(String id) {
        log.info("Going to query product with product series: {}", id);
        return jdbcTemplate.query("SELECT * FROM product_v2_tbl p " +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest " +
                " on p.id = latest.id and p.version = latest.mversion " +
                " WHERE p.seriesID = ?", rowMapper, id);
    }

    public void createProduct(Product product) {
        if (seriesRespository.querySeriesByID(product.getSeriesID()).isPresent()) {
            saveProduct(product);
        } else {
            throw new RuntimeException("Series id " + product.getSeriesID() + " is not existed");
        }
    }

    public void deleteProduct(String id) {
        log.info("Delete product for {}", id);
        jdbcTemplate.update("DELETE FROM product_v2_tbl where id = ?", id);


    }

    public void updateProduct(Product product) {
        log.info("Change product as {} ", product);
        String insertSql = "INSERT INTO product_v2_tbl (id, seriesID, name, isSpecial, stock, " +
                " productImage, productGrayImage, version) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int update = jdbcTemplate.update(insertSql,
                product.getId(),
                product.getSeriesID(),
                product.getName(),
                product.getIsSpecial(),
                product.getStock(),
                product.getProductImage(),
                product.getProductGrayImage(),
                product.getVersion());
        log.info("update row {} ", update);
    }


    public List<Product> getProductWithPriceBySeriesID(String id) {
        log.info("Going to query product with product series: {}", id);
        return jdbcTemplate.query(" SELECT p.id, p.seriesId, p.name, p.productImage, p.productGrayImage, price FROM product_v2_tbl p " +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion " +
                " inner join series_tbl as series on p.seriesId = series.id " +
                " WHERE p.seriesID = ?", rowMapper, id);
    }
}