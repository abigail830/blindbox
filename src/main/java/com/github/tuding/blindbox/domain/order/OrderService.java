package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.infrastructure.Constant;
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
    public void createOrder(String openId, String drawId, String ipAddress, Boolean useCoupon) {

        final Product product = productRepository.getProductWithPriceByDrawID(drawId)
                .orElseThrow(ProductNotFoundException::new);
        String orderId = UUID.randomUUID().toString();
        log.info("Going to place order[{}] for product: {}", orderId, product);

        try {
            Order oriOder = new Order(orderId, product.getName(), product.getPrice(), openId, drawId);
            ;
            if (useCoupon) {
                oriOder.setProductPrice(product.getPrice().multiply(Constant.DISCOUNT));
            }
            final Order order = wxPayment.generatePayment(oriOder, ipAddress);
            orderRepository.save(order);
        } catch (Exception e) {
            throw new BizException(ErrorCode.FAIL_TO_PLACE_ORDER);
        }
    }
}
