package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.domain.product.DrawService;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.exception.OrderNotFoundException;
import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.infrastructure.client.payment.WxPayment;
import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    //2 hours
    public static long ORDER_TIMEOUT = 7200000;

    @Autowired
    WxPayment wxPayment;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DrawRepository drawRepository;

    @Autowired
    DrawService drawService;

    private final ScheduledExecutorService scheduledExecutorService
            = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("order-auto-scan-thread-%d").build());

    public OrderService() {
        scheduledExecutorService.scheduleWithFixedDelay(this::scanExpiredOrder, 1, 2, TimeUnit.HOURS);
    }

    public void scanExpiredOrder() {
        try {
            log.info("Start to scan timeout order. ");
            List<Order> orders = orderRepository.getAllOutstandingOrder();
            List<Order> timeoutOrders = orders.stream()
                    .filter(item -> item.getStatus().equalsIgnoreCase(OrderStatus.NEW.name()))
                    .filter(item -> System.currentTimeMillis() - item.getCreateTime().getTime() > ORDER_TIMEOUT)
                    .collect(Collectors.toList());
            for (Order order : timeoutOrders) {
                log.info("Handler expired order {}", order);
                handleExpiredOrder(order);
            }
        } catch (Exception ex) {
            log.error("Failed to handle order scan job. ", ex);
        }
    }

    public void handleExpiredOrder(Order order) {
        updateOrderToPayExpired(order.orderId);
        drawService.cancelADrawByDrawId(order.getDrawId());
    }

    @Transactional
    public Order createProductOrder(String openId, String drawId, String ipAddress) {

        final Product product = productRepository.getProductWithPriceByDrawID(drawId)
                .orElseThrow(ProductNotFoundException::new);
        String orderId = UUID.randomUUID().toString();
        log.info("Going to place order[{}] for product: {}", orderId, product);

        try {
            Order preOder = new Order(orderId, product.getName(), product.getPrice(), openId, drawId);
            //place order to wxchat
            final Order orderWithWxInfo = wxPayment.generatePayment(preOder, ipAddress);
            //TODO: now addr info null should cause problem in save?
            drawRepository.confirmDrawToOrder(drawId);
            orderRepository.save(orderWithWxInfo);
            return orderWithWxInfo;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL_TO_PLACE_ORDER_TO_WX);
        }
    }

    public Order getOrder(String orderId) {
        return orderRepository.getOrder(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public void updateOrderToPaySuccess(String orderId) {
        log.info("Going to update order[{}] to {}", orderId, OrderStatus.PAY_PRODUCT_SUCCESS.name());
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAY_PRODUCT_SUCCESS.name());
    }

    public void updateOrderToPayFail(String orderId) {
        log.info("Going to update order[{}] to {}", orderId, OrderStatus.PAY_PRODUCT_FAIL.name());
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAY_PRODUCT_FAIL.name());
    }

    public void updateOrderToPayExpired(String orderId) {
        log.info("Going to update order[{}] to {}", orderId, OrderStatus.PAY_PRODUCT_EXPIRY.name());
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAY_PRODUCT_EXPIRY.name());
    }

    public TransportOrder payTransportOrder(TransportOrder transportOrder, String ipAddr) {
        final TransportOrder tOrder = (TransportOrder) wxPayment.generatePayment(transportOrder, ipAddr);
        orderRepository.updateOrderStatusAndAddress(tOrder);
        return tOrder;
    }

    public void updateOrderToTransportPaySuccess(String transportOrderId) {
        orderRepository.updateOrderStatusByTransportOrderId(transportOrderId,
                OrderStatus.PAY_TRANSPORT_SUCCESS.name());
    }

    public void updateOrderToTransportPayFail(String transportOrderId) {
        orderRepository.updateOrderStatusByTransportOrderId(transportOrderId,
                OrderStatus.PAY_TRANSPORT_FAIL.name());
    }
}
