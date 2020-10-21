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
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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

    @Autowired
    UserRepository userRepository;

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
                    .filter(item -> item.getStatus().equalsIgnoreCase(OrderStatus.NEW.name())
                            || item.getStatus().equalsIgnoreCase(OrderStatus.NEW_TRANSPORT.name()))
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
        if (OrderStatus.NEW_TRANSPORT.name().equals(order.getStatus())) {
            log.info("Going to update order[{}] to {}", order.getOrderId(), OrderStatus.PAY_TRANSPORT_EXPIRY.name());
            orderRepository.updateOrderStatus(order.getOrderId(), OrderStatus.PAY_TRANSPORT_EXPIRY.name());
        } else {
            log.info("Going to update order[{}] to {}", order.getOrderId(), OrderStatus.PAY_PRODUCT_EXPIRY.name());
            orderRepository.updateOrderStatus(order.getOrderId(), OrderStatus.PAY_PRODUCT_EXPIRY.name());
            drawService.cancelADrawByDrawId(order.getDrawId());
        }
    }


    @Transactional
    public Order createProductOrder(String openId, String drawId, String ipAddress) {

        final Product product = productRepository.getProductWithPriceByDrawID(drawId)
                .orElseThrow(ProductNotFoundException::new);

        try {
            Order preOder = new Order(product.getName(), product.getPrice(), openId, drawId);
            log.info("Going to place order[{}] for product: {}", preOder.getOrderId(), product);
            final Order orderWithWxInfo = wxPayment.generatePayment(preOder, ipAddress);
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

    public void updateOrderToPayFail(String orderId) {
        log.info("Going to update order[{}] to {}", orderId, OrderStatus.PAY_PRODUCT_FAIL.name());
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAY_PRODUCT_FAIL.name());
    }

    private TransportOrder payTransportOrder(TransportOrder transportOrder, String ipAddr) {
        TransportOrder tOrder = transportOrder;
        if (transportOrder.getProductPrice().compareTo(BigDecimal.ZERO) > 0) {
            orderRepository.updateOrderStatusAndAddress(tOrder, OrderStatus.NEW_TRANSPORT.name());
            tOrder = (TransportOrder) wxPayment.generatePayment(transportOrder, ipAddr);
        } else {
            orderRepository.updateOrderStatusAndAddress(tOrder, OrderStatus.PAY_TRANSPORT_SUCCESS.name());
        }
        return tOrder;
    }

    public TransportOrder processTransport(TransportOrder orders, String ipAddr) {
        final List<String> productOrders = orders.getProductOrders();
        if (isAllOrderPayed(productOrders)) {
            return payTransportOrder(orders, ipAddr);
        } else {
            log.warn("Order [{}] not pay yet or pay transport in progress", productOrders);
            throw new BizException(ErrorCode.INVALID_STATUS);
        }
    }


    public void updateOrderToTransportPaySuccess(String transportOrderId) {
        orderRepository.updateOrderStatusByTransportOrderId(transportOrderId,
                OrderStatus.PAY_TRANSPORT_SUCCESS.name());
    }

    public void updateOrderToTransportPayFail(String transportOrderId) {
        orderRepository.updateOrderStatusByTransportOrderId(transportOrderId,
                OrderStatus.PAY_TRANSPORT_FAIL.name());
    }

    public List<OrderWithProductInfo> getOrderDeliveredByOpenId(String openId) {
        return orderRepository.getOrderDelivered(openId);
    }

    public List<OrderWithProductInfo> getOrderPendingDeliverByOpenId(String openId) {
        return orderRepository.getOrderPendingDeliver(openId);
    }

    public List<OrderWithProductInfo> getOrderPendingPayTransportByOpenId(String openId) {
        return orderRepository.getOrderPendingPayTransportFee(openId);
    }

    private boolean isAllOrderPayed(List<String> productOrders) {
        Integer count = orderRepository.getPayedOrderCount(productOrders);
        return productOrders.size() == count;
    }
}
