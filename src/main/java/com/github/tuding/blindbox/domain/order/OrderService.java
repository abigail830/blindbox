package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.infrastructure.client.WxPayment;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class OrderService {

    @Autowired
    WxPayment wxPayment;

    @Autowired
    ProductRepository productRepository;

    public void createOrder(String openId, String productId, String ipAddress) {

        final Product product = productRepository.getProductByID(productId)
                .orElseThrow(ProductNotFoundException::new);

        //TODO: generate order and save in DB?
        String orderId = UUID.randomUUID().toString();

        //post to wxchat client
        wxPayment.generatePayment(openId, product, orderId, ipAddress);


    }
}
