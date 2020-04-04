package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);

    public void save(Order order) {
        log.info("Going to save order {}", order);
    }
}
