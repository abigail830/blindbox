package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.user.TransportFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TransportFeeRepository {

    List<TransportFee> transportFeeList;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RowMapper<TransportFee> rowMapper = new BeanPropertyRowMapper<>(TransportFee.class);

    @PostConstruct
    public void init() {
        transportFeeList = getAllTransportFee();
    }

    public List<TransportFee> getTransportFeeList() {
        return transportFeeList;
    }

    List<TransportFee> getAllTransportFee() {
        String sql = "select * from transport_fee_tbl";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
