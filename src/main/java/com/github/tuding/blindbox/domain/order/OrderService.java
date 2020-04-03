package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.infrastructure.client.payment.WxPayment;
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

    @Transactional
    public void createOrder(String openId, String drawId, String ipAddress) {

        final Product product = productRepository.getProductByDrawID(drawId)
                .orElseThrow(ProductNotFoundException::new);

        //TODO: generate order and save in DB?
        String orderId = UUID.randomUUID().toString();
        log.info("Going to place order[{}] for product: {}", orderId, product);

        //TODO: pending id for test
        try {
            wxPayment.generatePayment(new Order(orderId, product, openId, drawId), ipAddress);
        } catch (Exception e) {

        }
    }
}
