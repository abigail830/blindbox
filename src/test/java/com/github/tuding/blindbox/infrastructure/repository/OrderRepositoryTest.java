package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DBRider
class OrderRepositoryTest {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DataSet("test-data/empty-order.yml")
    void getOrderPendingPayTransportFee() {

        Toggle.TEST_MODE.setStatus(true);
        orderRepository.save(new Order("testOrderID", "openId",
                "d7493a65-1b22-479e-b3fe-decd80abe325", "test",
                BigDecimal.valueOf(100), "test", "test", "test", "test",
                "receiver", "12345678901", "area", "associateCode",
                "detailAddress", OrderStatus.PAY_PRODUCT_SUCCESS.name(), new Date(), null, null));

        final List<Order> orders = orderRepository.getOrderPendingPayTransportFee("openId");
        System.out.println(orders);
        assertEquals(1, orders.size());
    }
}