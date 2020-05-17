package com.github.tuding.blindbox.domain.order;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.user.User;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DBRider
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DataSet("test-data/add-bonus-when-purchase.yml")
    void updateOrderToPaySuccess() {
        orderService.updateOrderToPaySuccess("orderId1");

        final Order order = orderService.getOrder("orderId1");
        assertEquals(OrderStatus.PAY_PRODUCT_SUCCESS.name(), order.getStatus());

        final Optional<User> user = userRepository.getUserByOpenId("openId1");
        assertTrue(user.isPresent());
        assertEquals(20, user.get().getBonus());
    }
}