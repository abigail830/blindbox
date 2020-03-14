package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.ShippingAddress;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@Slf4j
public class ShippingAddressRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ShippingAddress> rowMapper = new BeanPropertyRowMapper<>(ShippingAddress.class);

    public ShippingAddress saveAddress(ShippingAddress shippingAddress) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("Going to insert shipping_addr_tbl with addr: {}", shippingAddress);

        String sql = "INSERT INTO shipping_addr_tbl " +
                "(receiver, mobile, area, associate_code, detail_address, is_default_address, open_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int update = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, shippingAddress.getReceiver());
                    ps.setString(2, shippingAddress.getMobile());
                    ps.setString(3, shippingAddress.getArea());
                    ps.setString(4, shippingAddress.getAssociateCode());
                    ps.setString(5, shippingAddress.getDetailAddress());
                    ps.setBoolean(6, shippingAddress.getIsDefaultAddress());
                    ps.setString(7, shippingAddress.getOpenId());
                    return ps;
                }, keyHolder);

        final long id = extractID(keyHolder);
        shippingAddress.setId(id);
        log.info("Inserted {} into DB.", shippingAddress);

        return shippingAddress;
    }

    private long extractID(KeyHolder keyHolder) {
        final Number key = keyHolder.getKey();
        if (key != null) {
            return key.longValue();
        } else {
            throw new BizException(ErrorCode.FAIL_TO_MODIFY_SHIPPING_ADDRESS);
        }
    }

    public void removeDefaultForOther(ShippingAddress shippingAddress) {
        String sql = "UPDATE shipping_addr_tbl set is_default_address = false " +
                "WHERE open_id = \'" + shippingAddress.getOpenId() +
                "\' AND id != " + shippingAddress.getId();
        jdbcTemplate.update(sql);
    }

    public List<ShippingAddress> getAllAddress() {
        return jdbcTemplate.query("SELECT * FROM shipping_addr_tbl", rowMapper);
    }

    public List<ShippingAddress> getAddressByOpenId(String openId) {
        return jdbcTemplate.query("SELECT * FROM shipping_addr_tbl WHERE open_id = ?", rowMapper, openId);
    }

    public void deleteAddressByOpenIdAndAddrId(String openId, String addrId) {
        log.info("delete address[{}] for user {}", addrId, openId);
        jdbcTemplate.update("DELETE FROM shipping_addr_tbl WHERE id = ? and open_id=?", addrId, openId);
    }

}
