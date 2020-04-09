package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin-ui/orders")
@Slf4j
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

    @PostMapping("/deliver/{orderId}")
    public
    String placeOrder(Model model,
                      @PathVariable String orderId,
                    @RequestParam("shippingCompany") String shippingCompany,
                    @RequestParam("shippingTicket") String shippingTicket) {
        log.info("confirm deliver for {} {} {}", orderId, shippingCompany, shippingTicket);
        orderRepository.updateOrderDeliveryStatus(orderId,
                OrderStatus.DELIVERED.name(), shippingCompany, shippingTicket);
        List<Order> allOutstandingOrder =
                orderRepository.getAllOrder();

        model.addAttribute("orders", allOutstandingOrder);
        return "order";
    }
}
