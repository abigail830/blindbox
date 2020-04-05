package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.user.ShippingAddress;
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
                "(receiver, mobile, area, associate_code, detail_address, is_default_address, province, open_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, shippingAddress.getReceiver());
                    ps.setString(2, shippingAddress.getMobile());
                    ps.setString(3, shippingAddress.getArea());
                    ps.setString(4, shippingAddress.getAssociateCode());
                    ps.setString(5, shippingAddress.getDetailAddress());
                    ps.setBoolean(6, shippingAddress.getIsDefaultAddress());
                    ps.setString(7, shippingAddress.getProvince());
                    ps.setString(8, shippingAddress.getOpenId());
                    return ps;
                }, keyHolder);

        final long id = extractID(keyHolder, shippingAddress.getOpenId());
        shippingAddress.setId(id);
        log.info("Inserted {} into DB.", shippingAddress);

        return shippingAddress;
    }

    private long extractID(KeyHolder keyHolder, String openId) {
        final Number key = keyHolder.getKey();
        if (key != null) {
            return key.longValue();
        } else {
            log.warn("Fail to add address for user [{}]", openId);
            throw new BizException(ErrorCode.FAIL_TO_MODIFY_SHIPPING_ADDRESS);
        }
    }

    public void removeDefaultFlagForOther(ShippingAddress shippingAddress) {
        String sql = "UPDATE shipping_addr_tbl set is_default_address = false " +
                "WHERE open_id = ? AND id != ?";
        jdbcTemplate.update(sql, shippingAddress.getOpenId(), shippingAddress.getId());
    }

    public List<ShippingAddress> getAllAddress() {
        return jdbcTemplate.query("SELECT * FROM shipping_addr_tbl", rowMapper);
    }

    public List<ShippingAddress> getAddressByOpenId(String openId) {
        return jdbcTemplate.query("SELECT * FROM shipping_addr_tbl WHERE open_id = ?", rowMapper, openId);
    }

    public int deleteAddressByOpenIdAndAddrId(String openId, long addrId) {
        log.info("delete address[{}] for user {}", addrId, openId);
        return jdbcTemplate.update("DELETE FROM shipping_addr_tbl WHERE id = ? and open_id=?", addrId, openId);
    }

    public void updateLastAddrAsDefault(String openId) {
        String sql = "UPDATE shipping_addr_tbl set is_default_address = true " +
                "WHERE id = (SELECT ID from shipping_addr_tbl WHERE open_id = ? ORDER BY ID DESC LIMIT 1)";
        jdbcTemplate.update(sql, openId);
        log.info("Update latest address(if there is any) as default for user [{}]", openId);
    }

    public int updateAddress(ShippingAddress shippingAddress) {
        String sql = "UPDATE shipping_addr_tbl set receiver = ?, mobile = ?, area = ?, " +
                "associate_code = ?, detail_address = ?, is_default_address = ?, province = ?, " +
                "WHERE id = ? and open_id = ?";
        return jdbcTemplate.update(sql,
                shippingAddress.getReceiver(), shippingAddress.getMobile(), shippingAddress.getArea(),
                shippingAddress.getAssociateCode(), shippingAddress.getDetailAddress(),
                shippingAddress.getIsDefaultAddress(), shippingAddress.getProvince(),
                shippingAddress.getId(), shippingAddress.getOpenId());
    }
}
