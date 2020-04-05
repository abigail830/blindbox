package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.domain.order.TransportOrder;
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
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);

    public void save(Order order) {
        log.info("Going to save order {}", order);
        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO order_tbl (orderId, openId, drawId, productName, productPrice, " +
                    " receiver, mobile, area, associateCode," +
                    " detailAddress, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, order.getOrderId(), order.getOpenId(), order.getDrawId(),
                    order.getProductName(), order.getProductPrice(), order.getReceiver(),
                    order.getMobile(), order.getArea(), order.getAssociateCode(), order.getDetailAddress(),
                    order.getStatus());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO order_tbl (orderId, openId, drawId, productName, productPrice, " +
                    " receiver, mobile, area, associateCode," +
                    " detailAddress, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, order.getOrderId(), order.getOpenId(), order.getDrawId(),
                    order.getProductName(), order.getProductPrice(), order.getReceiver(),
                    order.getMobile(), order.getArea(), order.getAssociateCode(), order.getDetailAddress(),
                    order.getStatus());
            log.info("update row {} ", update);
        }
    }

    public List<Order> getAllOrder() {
        log.info("Get all order ");
        return jdbcTemplate.query("SELECT * FROM order_tbl", rowMapper);
    }

    public List<Order> getAllOutstandingOrder() {
        log.info("Get all outstanding order ");
        return jdbcTemplate.query("SELECT * FROM order_tbl WHERE status = ?", rowMapper,
                OrderStatus.NEW.name());
    }

    public List<Order> getAllOutstandingOrderByOpenId(String openId) {
        log.info("Get all outstanding order ");
        return jdbcTemplate.query("SELECT * FROM order_tbl WHERE status = ? AND openId = ?", rowMapper,
                OrderStatus.NEW.name(), openId);
    }

    public void updateOrderStatus(String orderId, String status) {
        log.info("Update order id {} status to {}", orderId, status);
        jdbcTemplate.update("UPDATE order_tbl SET status = ? WHERE orderId = ?", status, orderId);

    }

    public void updateOrderStatusAndAddress(TransportOrder transportOrder) {
        transportOrder.getProductOrders().forEach(orderId -> {
            updateOrderStatusAndAddress(OrderStatus.NEW_TRANSPORT.name(), orderId, transportOrder);
        });
    }

    private void updateOrderStatusAndAddress(String status, String orderId, TransportOrder transportOrder) {
        String sql = "UPDATE order_tbl SET status = ?, receiver = ?,mobile = ?, area = ?, associateCode = ?," +
                " detailAddress=?, tranportOrderId = ? WHERE orderId = ?";
        jdbcTemplate.update(sql, status, transportOrder.getReceiver(), transportOrder.getMobile(),
                transportOrder.getArea(), transportOrder.getAssociateCode(),
                transportOrder.getDetailAddress(), transportOrder.getOrderId(), orderId);
    }

    public void updateOrderStatusByTransportOrderId(String transportOrderId, String status) {
        String sql = "UPDATE order_tbl SET status = ? WHERE tranportOrderId = ?";
        jdbcTemplate.update(sql, status, transportOrderId);

    }

    public Optional<Order> getOrder(String orderId) {
        log.info("Get all order by id {}", orderId);
        List<Order> orders = jdbcTemplate.query("SELECT * FROM order_tbl WHERE orderId = ?", rowMapper,
                orderId);
        return orders.stream().findFirst();
    }
}
