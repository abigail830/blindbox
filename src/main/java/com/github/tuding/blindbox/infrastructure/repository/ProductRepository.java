package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.Product;
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
        log.info("Going to insert product_tbl for {}", product);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO product_tbl (id, seriesID, name, isSpecial, isPresale, stock, " +
                    " probability, productImage, postCardImage, productGrayImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, product.getId(), product.getSeriesID(), product.getName(),
                    product.getIsSpecial(), product.getIsPresale(), product.getStock(), product.getProbability(),
                    product.getProductImage(), product.getPostCardImage(), product.getProductGrayImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO product_tbl (id, seriesID, name, isSpecial, isPresale, stock, " +
                    " probability, productImage, postCardImage, productGrayImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, product.getId(), product.getSeriesID(), product.getName(),
                    product.getIsSpecial(), product.getIsPresale(), product.getStock(), product.getProbability(),
                    product.getProductImage(), product.getPostCardImage(), product.getProductGrayImage());
            log.info("update row {} ", update);
        }
    }

    public Optional<Product> getProductByID(String id) {
        log.info("Going to query product with id: {}", id);
        List<Product> products = jdbcTemplate.query("SELECT * FROM product_tbl WHERE id = ?", rowMapper, id);
        return products.stream().findFirst();
    }

    public List<Product> getProductBySeries(String id) {
        log.info("Going to query product with product series: {}", id);
        return jdbcTemplate.query("SELECT * FROM product_tbl WHERE seriesID = ?", rowMapper, id);
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
        jdbcTemplate.update("DELETE FROM product_tbl where id = ?", id);


    }

    public void updateProduct(Product product) {
        String updateSql = "UPDATE product_tbl " +
                " SET name = ?, isSpecial = ?, stock = ?, probability = ?" +
                " WHERE id = ?";
        int update = jdbcTemplate.update(updateSql, product.getName(), product.getIsSpecial(),
                product.getStock(), product.getProbability(), product.getId());
        log.info("update row {} ", update);
    }
}
