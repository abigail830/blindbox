package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.infrastructure.client.payment.WxPayment;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class OrderService {

    @Autowired
    WxPayment wxPayment;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

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
            orderRepository.save(orderWithWxInfo);
            return orderWithWxInfo;
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL_TO_PLACE_ORDER_TO_WX);
        }
    }

    public void updateOrderToPaySuccess(String orderId) {
        log.info("Going to update order[{}] to {}", orderId, OrderStatus.PAY_PRODUCT_SUCCESS.name());
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAY_PRODUCT_SUCCESS.name());
    }

    public void updateOrderToPayFail(String orderId) {
        log.info("Going to update order[{}] to {}", orderId, OrderStatus.PAY_PRODUCT_FAIL.name());
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAY_PRODUCT_FAIL.name());
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
