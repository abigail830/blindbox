package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.OrderDTO;
import com.github.tuding.blindbox.api.admin.dto.OrderDTOWrapper;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
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

    @GetMapping("/v2/items/{status}/{orderId}/{receiver}/{mobile}")
    public ResponseEntity<OrderDTOWrapper> getItems(@PathVariable String status,
                                                    @PathVariable String orderId,
                                                    @PathVariable String receiver,
                                                    @PathVariable String mobile,
                                                    @RequestParam("pageSize") int pageSize,
                                                    @RequestParam("pageNumber") int pageNumber) {
        log.info("query order paging for status {} orderId {} receiver {} mobile {}, pageSize {}, pageNumber {}",
                status, orderId, receiver, mobile, pageSize, pageNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .body(new OrderDTOWrapper(orderRepository.queryOrderWithDetail(UI_STATUS_FILTER_MAP.get(status),
                        orderId,
                        receiver,
                        mobile,
                        pageSize,
                        pageSize * (pageNumber - 1))
                        .stream().map(OrderDTO::new).collect(Collectors.toList())));
    }


    @GetMapping("/v2/csv/{status}/{orderId}/{receiver}/{mobile}")
    public ResponseEntity<StreamingResponseBody> getItemsAsCsv(@PathVariable String status,
                                                               @PathVariable String orderId,
                                                               @PathVariable String receiver,
                                                               @PathVariable String mobile,
                                                               final HttpServletResponse response) {
        log.info("query order csv for status {} orderId {} receiver {} mobile {}",
                status, orderId, receiver, mobile);
        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment;filename=result.csv");
        return new ResponseEntity(orderRepository.queryOrderWithDetailAsCsv(UI_STATUS_FILTER_MAP.get(status), orderId, receiver, mobile),
                HttpStatus.OK);
    }

    @GetMapping("/v2/count/{status}/{orderId}/{receiver}/{mobile}")
    public ResponseEntity<String> getCount(@PathVariable String status,
                                           @PathVariable String orderId,
                                           @PathVariable String receiver,
                                           @PathVariable String mobile) {
        log.info("query order paging for status {} orderId {} receiver {} mobile {}",
                status, orderId, receiver, mobile);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("count", orderRepository.getTotalCount(UI_STATUS_FILTER_MAP.get(status), orderId, receiver, mobile));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .body(jsonObject.toString());
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

    @PostMapping("/v2/deliver/{orderId}")
    public
    ResponseEntity<String>   placeOrder2(
                      @PathVariable String orderId,
                      @RequestParam("shippingCompany") String shippingCompany,
                      @RequestParam("shippingTicket") String shippingTicket) {
        log.info("v2 confirm deliver for {} {} {}", orderId, shippingCompany, shippingTicket);
        orderRepository.updateOrderDeliveryStatus(orderId,
                OrderStatus.DELIVERED.name(), shippingCompany, shippingTicket);

        return ResponseEntity.ok()
                .body("");
    }
}
