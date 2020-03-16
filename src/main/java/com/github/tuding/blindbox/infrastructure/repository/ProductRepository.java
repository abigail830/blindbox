package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
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

    private RowMapper<ProductDTO> rowMapper = new BeanPropertyRowMapper<>(ProductDTO.class);

    private void saveProduct(ProductDTO productDTO) {
        log.info("Going to insert product_tbl for {}", productDTO);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO product_tbl (seriesID, name, isSpecial, isPresale, stock, " +
                    " probability, productImage, postCardImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, productDTO.getSeriesID(), productDTO.getName(),
                    productDTO.isSpecial(), productDTO.isPresale(), productDTO.getStock(), productDTO.getProbability(),
                    productDTO.getProductImage(), productDTO.getPostCardImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO product_tbl (seriesID, name, isSpecial, isPresale, stock, " +
                    " probability, productImage, postCardImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, productDTO.getSeriesID(), productDTO.getName(),
                    productDTO.isSpecial(), productDTO.isPresale(), productDTO.getStock(), productDTO.getProbability(),
                    productDTO.getProductImage(), productDTO.getPostCardImage());
            log.info("update row {} ", update);
        }
    }

    public Optional<ProductDTO> getProductByName(String name) {
        log.info("Going to query product with name: {}", name);
        List<ProductDTO> productDTOs = jdbcTemplate.query("SELECT * FROM product_tbl WHERE name = ?", rowMapper, name);
        return productDTOs.stream().findFirst();
    }

    public List<ProductDTO> getProductBySeries(Long id) {
        log.info("Going to query product with product series: {}", id);
        return jdbcTemplate.query("SELECT * FROM product_tbl WHERE seriesID = ?", rowMapper, id);
    }

    public void createProduct(ProductDTO productDTO) {
        if (seriesRespository.querySeriesByID(productDTO.getSeriesID()).isPresent()) {
            saveProduct(productDTO);
        } else {
            throw new RuntimeException("Series id " + productDTO.getSeriesID() + " is not existed");
        }
    }

    public void deleteProduct(String name) {
        log.info("Delete product for {}", name);
        jdbcTemplate.update("DELETE FROM product_tbl where name = ?", name);


    }
}
