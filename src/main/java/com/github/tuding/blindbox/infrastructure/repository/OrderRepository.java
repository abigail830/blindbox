package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.domain.order.OrderWithProductInfo;
import com.github.tuding.blindbox.domain.order.TransportOrder;
import com.github.tuding.blindbox.domain.wx.callback.OrderSimpleInfo;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.util.StreamingCsvResultSetExt;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.OutputStream;
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

    private RowMapper<OrderSimpleInfo> orderSimpleInfoRowMapper = new BeanPropertyRowMapper<>(OrderSimpleInfo.class);

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

    public Order updateOrderStatusAndQueryBack(String orderId, String status) {
        log.info("Update order id {} status to {}", orderId, status);
        jdbcTemplate.update("UPDATE order_tbl SET status = ? WHERE orderId = ?", status, orderId);
        return getOrder(orderId).orElseThrow(() -> new BizException(ErrorCode.FAIL_TO_GET_ORDER));
    }

    public List<OrderSimpleInfo> getOrderInfoByOpenIdAndDrawId(String openId, String drawId) {

        final List<String> status = Arrays.asList(OrderStatus.NEW.name(),
                OrderStatus.PAY_PRODUCT_EXPIRY.name(), OrderStatus.PAY_PRODUCT_FAIL.name());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("openId", openId);
        parameters.addValue("drawId", drawId);
        parameters.addValue("status", status);

        String sql = "select productId, createTime, draw_tbl.seriesId from order_tbl join draw_tbl\n" +
                "    where order_tbl.drawId = draw_tbl.drawId and order_tbl.openId = (:openId) and draw_tbl.seriesId in " +
                "(select seriesId from draw_tbl where drawId = (:drawId)) \n" +
                "    and order_tbl.status not in (:status) order by createTime asc";
        return namedParameterJdbcTemplate.query(sql, parameters, orderSimpleInfoRowMapper);
    }

    public void updateOrderDeliveryStatus(String orderId, String status, String shippingCompany, String shippingTicket) {
        log.info("Update order id {} status to {}", orderId, status);
        jdbcTemplate.update("UPDATE order_tbl SET status = ?, shippingCompany = ?, shippingTicket = ? WHERE orderId = ?",
                status, shippingCompany, shippingTicket, orderId);

    }

    public void updateOrderStatusAndAddress(TransportOrder transportOrder, String status) {
        transportOrder.getProductOrders().forEach(orderId -> {
            updateOrderStatusAndAddress(status, orderId, transportOrder);
        });
    }

    private void updateOrderStatusAndAddress(String status, String orderId, TransportOrder transportOrder) {
        log.info("Going to update order {} with status {} and transportId {}", orderId, status, transportOrder);
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
                        " WHERE o.status in (?, ?, ?)  AND o.openId = ? order by o.createTime desc",
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
                        " WHERE o.status =? AND o.openId = ? order by o.createTime desc",
                rowMapperWithProduct, OrderStatus.PAY_TRANSPORT_SUCCESS.name(), openId);
    }

    public List<OrderWithProductInfo> getOrderDelivered(String openId) {
        log.info("Get order pending deliver for user [{}].", openId);
        return jdbcTemplate.query("SELECT p.productImage, o.* FROM product_v2_tbl p" +
                        " inner join (select id, max(version) as mversion from product_v2_tbl group by id) latest  on p.id = latest.id and p.version = latest.mversion" +
                        " inner join draw_tbl d on p.ID = d.productId" +
                        " inner join order_tbl o on o.drawId = d.drawId" +
                        " WHERE o.status =? AND o.openId = ? order by o.createTime desc",
                rowMapperWithProduct, OrderStatus.DELIVERED.name(), openId);
    }

    public Integer getPayedOrderCount(List<String> productOrders) {
        log.info("Valid order status if ready for pay transport fee: [{}].", productOrders);

        List<String> status = Arrays.asList(OrderStatus.PAY_PRODUCT_SUCCESS.name(),
                OrderStatus.PAY_TRANSPORT_FAIL.name(),
                OrderStatus.PAY_TRANSPORT_EXPIRY.name());

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

    public List<Order> queryOrderWithPaging(List<String> status, Integer limitPerPage, Integer numOfPage) {
        log.info("Going to query order with status {} limit {} page {}", status, limitPerPage, numOfPage);
        if ("ALL".equalsIgnoreCase(status.get(0))) {
            return jdbcTemplate.query("SELECT * FROM order_tbl order by createTime desc LIMIT ? OFFSET ?",
                    rowMapper, limitPerPage, numOfPage);
        } else {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("status", status);
            parameters.addValue("limit", limitPerPage);
            parameters.addValue("offset", numOfPage);
            return namedParameterJdbcTemplate.query("SELECT * FROM order_tbl where status in (:status) order by createTime LIMIT :limit OFFSET :offset",
                    parameters, rowMapper);
        }
    }

    public Integer getTotalCount(List<String> status) {
        log.info("Going to item count with status {}", status);
        if ("ALL".equalsIgnoreCase(status.get(0))) {
            return namedParameterJdbcTemplate.queryForObject("select count(1) from order_tbl",
                    new MapSqlParameterSource(), Integer.class);
        } else {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("status", status);
            return namedParameterJdbcTemplate.queryForObject("select count(1) from order_tbl  where status in (:status)",
                    parameters, Integer.class);
        }
    }

    public MapSqlParameterSource buildParam(List<String> status, String orderID, String receiver, String mobile,
                                            Integer limitPerPage, Integer numOfPage) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (!"ALL".equalsIgnoreCase(status.get(0))) {
            parameters.addValue("status", status);
        }
        if (!"*".equalsIgnoreCase(orderID)) {
            parameters.addValue("orderId", "%" + orderID + "%");
        }
        if (!"*".equalsIgnoreCase(receiver)) {
            parameters.addValue("receiver", receiver);
        }
        if (!"*".equalsIgnoreCase(mobile)) {
            parameters.addValue("mobile", mobile);
        }
        parameters.addValue("limit", limitPerPage);
        parameters.addValue("offset", numOfPage);
        return parameters;
    }

    public MapSqlParameterSource buildParam(List<String> status, String orderID, String receiver, String mobile) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (!"ALL".equalsIgnoreCase(status.get(0))) {
            parameters.addValue("status", status);
        }
        if (!"*".equalsIgnoreCase(orderID)) {
            parameters.addValue("orderId", "%" + orderID + "%");
        }
        if (!"*".equalsIgnoreCase(receiver)) {
            parameters.addValue("receiver", receiver);
        }
        if (!"*".equalsIgnoreCase(mobile)) {
            parameters.addValue("mobile", mobile);
        }
        return parameters;
    }

    public String buildSql(List<String> status, String orderID, String receiver, String mobile) {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM order_tbl");

        if ("ALL".equalsIgnoreCase(status.get(0))
                && !StringUtils.isNotBlank(orderID)
                && !StringUtils.isNotBlank(receiver)
                && !StringUtils.isNotBlank(mobile)) {
            stringBuilder.append(" order by createTime desc LIMIT ? OFFSET ?");
        } else {
            stringBuilder.append(" where ");
            if (!"ALL".equalsIgnoreCase(status.get(0))) {
                stringBuilder.append("  status in (:status) ");
            } else {
                stringBuilder.append("  status is not null ");
            }

            if (!"*".equalsIgnoreCase(orderID)) {
                stringBuilder.append(" and orderID like :orderId ");
            }

            if (!"*".equalsIgnoreCase(receiver)) {
                stringBuilder.append(" and receiver = :receiver ");
            }

            if (!"*".equalsIgnoreCase(mobile)) {
                stringBuilder.append(" and mobile = :mobile ");
            }
            stringBuilder.append(" order by createTime LIMIT :limit OFFSET :offset");
        }
        return stringBuilder.toString();
    }

    public String buildSqlForCsv(List<String> status, String orderID, String receiver, String mobile) {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM order_tbl");

        if ("ALL".equalsIgnoreCase(status.get(0))
                && !StringUtils.isNotBlank(orderID)
                && !StringUtils.isNotBlank(receiver)
                && !StringUtils.isNotBlank(mobile)) {
            stringBuilder.append(" order by createTime  ");
        } else {
            stringBuilder.append(" where ");
            if (!"ALL".equalsIgnoreCase(status.get(0))) {
                stringBuilder.append("  status in (:status) ");
            } else {
                stringBuilder.append("  status is not null ");
            }

            if (!"*".equalsIgnoreCase(orderID)) {
                stringBuilder.append(" and orderID like :orderId ");
            }

            if (!"*".equalsIgnoreCase(receiver)) {
                stringBuilder.append(" and receiver = :receiver ");
            }

            if (!"*".equalsIgnoreCase(mobile)) {
                stringBuilder.append(" and mobile = :mobile ");
            }
            stringBuilder.append(" order by createTime limit 1000000");
        }
        return stringBuilder.toString();
    }

    public List<Order> queryOrderWithDetail(List<String> status, String orderID, String receiver, String mobile,
                                            Integer limitPerPage, Integer numOfPage) {
        log.info("Going to query order with status {} orderID {} receiver {} mobile {}", status, orderID, receiver, mobile);
        String sql = buildSql(status, orderID, receiver, mobile);
        MapSqlParameterSource mapSqlParameterSource = buildParam(status, orderID, receiver, mobile, limitPerPage, numOfPage);
        log.info("Going to run sql {}", sql);

        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper);
    }

    public StreamingResponseBody queryOrderWithDetailAsCsv(List<String> status, String orderID, String receiver, String mobile) {
        return new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException, WebApplicationException {
                log.info("Going to query order with status {} orderID {} receiver {} mobile {}", status, orderID, receiver, mobile);
                String sql = buildSqlForCsv(status, orderID, receiver, mobile);
                MapSqlParameterSource mapSqlParameterSource = buildParam(status, orderID, receiver, mobile);
                log.info("Going to run sql {}", sql);
                namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new StreamingCsvResultSetExt(outputStream));
            }
        };

    }

    public String buildSqlCount(List<String> status, String orderID, String receiver, String mobile) {
        StringBuilder stringBuilder = new StringBuilder("SELECT count(1) FROM order_tbl");

        if ("ALL".equalsIgnoreCase(status.get(0))
                && !StringUtils.isNotBlank(orderID)
                && !StringUtils.isNotBlank(receiver)
                && !StringUtils.isNotBlank(mobile)) {
            return stringBuilder.toString();
        } else {
            stringBuilder.append(" where ");
            if (!"ALL".equalsIgnoreCase(status.get(0))) {
                stringBuilder.append("  status in (:status) ");
            } else {
                stringBuilder.append("  status is not null ");
            }

            if (!"*".equalsIgnoreCase(orderID)) {
                stringBuilder.append(" and orderID like :orderId ");
            }

            if (!"*".equalsIgnoreCase(receiver)) {
                stringBuilder.append(" and receiver = :receiver ");
            }

            if (!"*".equalsIgnoreCase(mobile)) {
                stringBuilder.append(" and mobile = :mobile ");
            }
            return stringBuilder.toString();
        }
    }

    public Integer getTotalCount(List<String> status, String orderID, String receiver, String mobile) {
        log.info("Going to query order count with status {} orderID {} receiver {} mobile {}", status, orderID, receiver, mobile);
        String sql = buildSqlCount(status, orderID, receiver, mobile);
        MapSqlParameterSource mapSqlParameterSource = buildParam(status, orderID, receiver, mobile, null, null);
        log.info("Going to run sql {}", sql);

        return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);
    }
}
