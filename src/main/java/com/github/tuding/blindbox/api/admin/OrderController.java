package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.OrderDTO;
import com.github.tuding.blindbox.api.admin.dto.OrderDTOWrapper;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
//        List<OrderDTO> allOutstandingOrder =
//                orderRepository.getAllOrder()
//                        .stream().map(OrderDTO::new).collect(Collectors.toList());
//
//        model.addAttribute("orders", allOutstandingOrder);
        return "order";
    }

    @GetMapping("/count")
    public ResponseEntity<String> getItems() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count", orderRepository.getTotalCount());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .body(jsonObject.toString());
    }

    @GetMapping("/items")
    public ResponseEntity<OrderDTOWrapper> getItems(@RequestParam("pageSize") int pageSize,
                                                    @RequestParam("pageNumber") int pageNumber) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .body(new OrderDTOWrapper(orderRepository.queryOrderWithPaging(pageSize, pageSize * (pageNumber - 1))
                        .stream().map(OrderDTO::new).collect(Collectors.toList())));
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
