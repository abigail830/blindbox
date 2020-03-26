package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.github.tuding.blindbox.infrastructure.Constant.DRAW_INIT_STATUS;

@Service
@Slf4j
public class DrawService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    DrawRepository drawRepository;

    public Draw drawAProduct(String openId, String seriesId) {
        log.info("Draw a product for {}", seriesId);
        List<Product> productBySeries = productRepository.getProductBySeries(seriesId);
        Product product = drawAProduct(productBySeries);
        product.setVersion(product.getVersion() + 1);
        UUID drawID = UUID.randomUUID();
        Draw draw = new Draw(openId, drawID.toString(), DRAW_INIT_STATUS,
                product.getId(), seriesId, new Date());
        drawRepository.persistADraw(product, draw);
        log.info("Product is drawn {} and locked {}", draw, product);
        return draw;
    }

    public Draw confirmADraw(String drawId) {
        //TODO: update draw to confirm
        return null;
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
