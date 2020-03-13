package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.ShippingAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class ShippingAddressRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ShippingAddress> rowMapper = new BeanPropertyRowMapper<>(ShippingAddress.class);

    public void saveAddress(ShippingAddress shippingAddress) {
        log.info("Going to insert shipping_addr_tbl with addr: {}", shippingAddress);

        String sql = "INSERT INTO shipping_addr_tbl " +
                "(receiver, mobile, area, associate_code, detail_address, is_default_address, open_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int update = jdbcTemplate.update(sql,
                shippingAddress.getReceiver(),
                shippingAddress.getMobile(),
                shippingAddress.getArea(),
                shippingAddress.getAssociateCode(),
                shippingAddress.getDetailAddress(),
                shippingAddress.getIsDefaultAddress(),
                shippingAddress.getOpenId());
        if (update == 1) {
            log.info("Inserted {} into DB.", shippingAddress);
        }
    }

    public List<ShippingAddress> getAllAddress() {
        return jdbcTemplate.query("SELECT * FROM shipping_addr_tbl", rowMapper);
    }

    public List<ShippingAddress> getAddressByOpenId(String openId) {
        return jdbcTemplate.query("SELECT * FROM shipping_addr_tbl WHERE open_id = ?", rowMapper, openId);
    }


}
