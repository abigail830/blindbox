package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.domain.order.OrderWithProductInfo;
import com.github.tuding.blindbox.domain.order.TransportOrder;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);

    private RowMapper<OrderWithProductInfo> rowMapperWithProduct = new BeanPropertyRowMapper<>(OrderWithProductInfo.class);

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

    public void updateOrderDeliveryStatus(String orderId, String status, String shippingCompany, String shippingTicket) {
        log.info("Update order id {} status to {}", orderId, status);
        jdbcTemplate.update("UPDATE order_tbl SET status = ?, shippingCompany = ?, shippingTicket = ? WHERE orderId = ?",
                status, shippingCompany, shippingTicket, orderId);

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

    public List<OrderWithProductInfo> getOrderPendingPayTransportFee(String openId) {
        log.info("Get order pending to pay transport fee for user [{}].", openId);
        return jdbcTemplate.query("SELECT p.productImage, o.* FROM product_v2_tbl p" +
                        " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion" +
                        " inner join draw_tbl d on p.ID = d.productId" +
                        " inner join order_tbl o on o.drawId = d.drawId" +
                        " WHERE o.status in (?, ?, ?) AND o.openId = ?",
                rowMapperWithProduct,
                OrderStatus.PAY_PRODUCT_SUCCESS.name(),
                OrderStatus.NEW_TRANSPORT.name(),
                OrderStatus.PAY_TRANSPORT_FAIL.name(),
                openId);
    }

    public List<OrderWithProductInfo> getOrderPendingDeliver(String openId) {
        log.info("Get order pending deliver for user [{}].", openId);
        return jdbcTemplate.query("SELECT p.productImage, o.* FROM product_v2_tbl p" +
                        " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion" +
                        " inner join draw_tbl d on p.ID = d.productId" +
                        " inner join order_tbl o on o.drawId = d.drawId" +
                        " WHERE o.status =? AND o.openId = ?",
                rowMapperWithProduct, OrderStatus.PAY_TRANSPORT_SUCCESS.name(), openId);
    }

    public List<OrderWithProductInfo> getOrderDelivered(String openId) {
        log.info("Get order pending deliver for user [{}].", openId);
        return jdbcTemplate.query("SELECT p.productImage, o.* FROM product_v2_tbl p" +
                        " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion" +
                        " inner join draw_tbl d on p.ID = d.productId" +
                        " inner join order_tbl o on o.drawId = d.drawId" +
                        " WHERE o.status =? AND o.openId = ?",
                rowMapperWithProduct, OrderStatus.DELIVERED.name(), openId);
    }

    public Integer getPayedOrderCount(List<String> productOrders) {
        log.info("Valid order status if ready for pay transport fee: [{}].", productOrders);

        List<String> status = Arrays.asList(OrderStatus.PAY_PRODUCT_SUCCESS.name(),
                OrderStatus.NEW_TRANSPORT.name(), OrderStatus.PAY_TRANSPORT_FAIL.name());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("orders", productOrders);
        parameters.addValue("status", status);

        String sql = "SELECT count(*) FROM product_v2_tbl p" +
                " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion" +
                " inner join draw_tbl d on p.ID = d.productId" +
                " inner join order_tbl o on o.drawId = d.drawId" +
                " WHERE o.status in (:status) AND o.orderId in (:orders)";
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }
}
