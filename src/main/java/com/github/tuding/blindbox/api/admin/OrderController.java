package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.OrderDTO;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin-ui/orders")
@Slf4j
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        List<OrderDTO> allOutstandingOrder =
                orderRepository.getAllOrder()
                        .stream().map(OrderDTO::new).collect(Collectors.toList());

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
        List<OrderDTO> allOutstandingOrder =
                orderRepository.getAllOrder()
                        .stream().map(OrderDTO::new).collect(Collectors.toList());

        model.addAttribute("orders", allOutstandingOrder);
        return "order";
    }
}
