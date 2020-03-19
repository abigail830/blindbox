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
            String insertSql = "INSERT INTO product_tbl (id, seriesID, name, isSpecial, isPresale, stock, " +
                    " probability, productImage, postCardImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, productDTO.getId(), productDTO.getSeriesID(), productDTO.getName(),
                    productDTO.getIsSpecial(), productDTO.getIsPresale(), productDTO.getStock(), productDTO.getProbability(),
                    productDTO.getProductImage(), productDTO.getPostCardImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO product_tbl (id, seriesID, name, isSpecial, isPresale, stock, " +
                    " probability, productImage, postCardImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, productDTO.getId(), productDTO.getSeriesID(), productDTO.getName(),
                    productDTO.getIsSpecial(), productDTO.getIsPresale(), productDTO.getStock(), productDTO.getProbability(),
                    productDTO.getProductImage(), productDTO.getPostCardImage());
            log.info("update row {} ", update);
        }
    }

    public Optional<ProductDTO> getProductByID(String id) {
        log.info("Going to query product with id: {}", id);
        List<ProductDTO> productDTOs = jdbcTemplate.query("SELECT * FROM product_tbl WHERE id = ?", rowMapper, id);
        return productDTOs.stream().findFirst();
    }

    public List<ProductDTO> getProductBySeries(String id) {
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

    public void deleteProduct(String id) {
        log.info("Delete product for {}", id);
        jdbcTemplate.update("DELETE FROM product_tbl where id = ?", id);


    }

    public void updateProduct(ProductDTO productDTO) {
        String updateSql = "UPDATE product_tbl " +
                " SET name = ?, isSpecial = ?, stock = ?, probability = ?," +
                " productImage = ?, postCardImage = ?"+
                " WHERE id = ?";
        int update = jdbcTemplate.update(updateSql, productDTO.getName(), productDTO.getIsSpecial(),
                productDTO.getStock(), productDTO.getProbability(),
                productDTO.getProductImage(), productDTO.getPostCardImage(), productDTO.getId());
        log.info("update row {} ", update);
    }
}
