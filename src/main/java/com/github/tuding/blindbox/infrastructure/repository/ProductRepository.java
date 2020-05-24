package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ProductRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private SeriesRepository seriesRepository;

    private RowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);

    private void saveProduct(Product product) {
        log.info("Going to insert product_v2_tbl for {}", product);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO product_v2_tbl (id, seriesID, name, isSpecial, stock, " +
                    " productImage, productGrayImage, weight) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, product.getId(), product.getSeriesID(), product.getName(),
                    product.getIsSpecial(), product.getStock(),
                    product.getProductImage(), product.getProductGrayImage(), product.getWeight());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO product_v2_tbl (id, seriesID, name, isSpecial, stock, " +
                    " productImage, productGrayImage, weight) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, product.getId(), product.getSeriesID(), product.getName(),
                    product.getIsSpecial(), product.getStock(),
                    product.getProductImage(), product.getProductGrayImage(), product.getWeight());
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
        if (seriesRepository.querySeriesByIDWithoutRoleIds(product.getSeriesID()).isPresent()) {
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
                " productImage, productGrayImage, weight, version) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int update = jdbcTemplate.update(insertSql,
                product.getId(),
                product.getSeriesID(),
                product.getName(),
                product.getIsSpecial(),
                product.getStock(),
                product.getProductImage(),
                product.getProductGrayImage(),
                product.getWeight(),
                product.getVersion());
        log.info("update row {} ", update);
    }

    @Deprecated
    public List<Product> getProductWithPriceBySeriesIDOld(String id) {
        log.info("Going to query product with product series: {}", id);
        return jdbcTemplate.query(" SELECT p.id, p.seriesId, p.name, p.productImage, p.productGrayImage, price, p.weight FROM product_v2_tbl p " +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion " +
                " inner join series_tbl as series on p.seriesId = series.id " +
                " WHERE p.seriesID = ?", rowMapper, id);
    }

    public List<Product> getProductWithPriceBySeriesID(String id) {
        log.info("Going to query product with product series: {}", id);
        return jdbcTemplate.query(" SELECT p.id, p.seriesId, p.name, p.productImage, p.productGrayImage, price, p.weight FROM product_v2_tbl p " +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion " +
                " inner join series_v2_tbl as series on p.seriesId = series.id " +
                " WHERE p.seriesID = ?", rowMapper, id);
    }

    public Optional<Product> getProductWithPriceByDrawID(String drawId) {
        String sql = "SELECT p.*, d.price FROM product_v2_tbl p" +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion" +
                " left join draw_tbl d on p.ID = d.productId where d.drawId =?";
        final List<Product> result = jdbcTemplate.query(sql, rowMapper, drawId);
        return result.stream().findFirst();
    }

    /* Only get product after pay successful */
    public Optional<Product> getProductWithoutPriceByDrawID(String drawId) {
        String sql = "SELECT p.* FROM product_v2_tbl p" +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion" +
                " inner join draw_tbl d on p.ID = d.productId" +
                " inner join order_tbl o on o.drawId = d.drawId" +
                " where d.drawId =? and o.status not in (?,?,?)";
        final List<Product> result = jdbcTemplate.query(sql, rowMapper, drawId,
                OrderStatus.NEW.name(), OrderStatus.PAY_PRODUCT_EXPIRY.name(), OrderStatus.PAY_PRODUCT_FAIL.name());
        return result.stream().findFirst();
    }

    public List<String> getThreeRandomProductName() {
        String sql = "select name from product_v2_tbl order by rand() limit 3";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public void reduceStock(List<String> productIDs) {
        long start = System.currentTimeMillis();
        List<String> sqlList = new ArrayList<>(productIDs.size());
        for (String productID : productIDs) {
            sqlList.add("insert product_v2_tbl " +
                    " select id, (version +1), seriesID, name, isSpecial, (stock -1), productImage, productGrayImage, now(), weight from " +
                    " product_v2_tbl " +
                    " where version =  (select max(version) as mv  from product_v2_tbl where id = '" + productID + "')" +
                    " and id = '" + productID + "'");
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        log.info("Updated product stock in {}", System.currentTimeMillis() - start);
    }

    public List<String> getProductIdWhichPayed(List<String> ids, String openId) {
        final List<String> status = Arrays.asList(OrderStatus.NEW.name(),
                OrderStatus.PAY_PRODUCT_EXPIRY.name(), OrderStatus.PAY_PRODUCT_FAIL.name());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        parameters.addValue("status", status);
        parameters.addValue("openId", openId);

        String sql = "select DISTINCT d.productId from draw_tbl d where" +
                " d.openId = (:openId) and" +
                " d.productId in (:ids) and" +
                " d.drawId in (select o.drawId from order_tbl o where o.status not in (:status))";
        return namedParameterJdbcTemplate.queryForList(sql, parameters, String.class);
    }
}