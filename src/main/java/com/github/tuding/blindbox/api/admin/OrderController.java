package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin-ui/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        List<Order> allOutstandingOrder =
                orderRepository.getAllOrder();

        model.addAttribute("orders", allOutstandingOrder);
        return "order";
    }

    @PutMapping("/deliver/{orderId}")
    public @ResponseBody
    void placeOrder(@PathVariable String orderId) {
        orderRepository.updateOrderStatus(orderId, OrderStatus.DELIVERED.name());
    }
}
