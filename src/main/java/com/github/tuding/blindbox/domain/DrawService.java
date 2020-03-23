package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DrawService {
    @Autowired
    ProductRepository productRepository;

    public Product drawAProduct(String seriesId) {
        log.info("Draw a product for {}", seriesId);
        List<Product> productBySeries = productRepository.getProductBySeries(seriesId);
        Product product = drawAProduct(productBySeries);
        //TODO: LOCKED STOCK HERE
        log.info("Product is drawn and locked {}", product);
        return product;
    }

    public static Product drawAProduct(List<Product> products) {
        long sum = products.stream().mapToLong(Product::getStock).sum();
        double rand = Math.random();
        double cumulativeP = 0.0d;
        for (Product product : products) {
            cumulativeP += (double)product.stock/(double)sum;
            if (rand <= cumulativeP) {
                return product;
            }
        }
        throw new RuntimeException("Failed to draw the product");
    }
}
