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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin-ui/orders")
@Slf4j
public class OrderController {
    Map<String, List<String>> UI_STATUS_FILTER_MAP = new HashMap<String, List<String>>(){{
        put("ALL", Arrays.asList("ALL"));
        put("DELIVERED", Arrays.asList("DELIVERED"));
        put("PAY_TRANSPORT_SUCCESS", Arrays.asList("PAY_TRANSPORT_SUCCESS"));
        put("PAY_PRODUCT_SUCCESS", Arrays.asList("PAY_PRODUCT_SUCCESS"));
        put("PENDING_PAY_TRANSPORT", Arrays.asList("NEW_TRANSPORT", "PAY_TRANSPORT_FAIL", "PAY_TRANSPORT_EXPIRY"));
    }};



    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        return "order";
    }

    @GetMapping("/count/{status}")
    public ResponseEntity<String> getItems(@PathVariable String status) {
        log.info("query order count for {} {} {}", status);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count", orderRepository.getTotalCount(UI_STATUS_FILTER_MAP.get(status)));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .body(jsonObject.toString());
    }

    @GetMapping("/items/{status}")
    public ResponseEntity<OrderDTOWrapper> getItems(@PathVariable String status,
                                                    @RequestParam("pageSize") int pageSize,
                                                    @RequestParam("pageNumber") int pageNumber) {
        log.info("query order paging for {} {} {}", status, pageSize, pageNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .body(new OrderDTOWrapper(orderRepository.queryOrderWithPaging(UI_STATUS_FILTER_MAP.get(status), pageSize, pageSize * (pageNumber - 1))
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
