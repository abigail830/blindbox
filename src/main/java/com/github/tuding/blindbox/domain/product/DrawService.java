package com.github.tuding.blindbox.domain.product;

import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.util.RetryUtil;
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
        Draw draw = RetryUtil.retryOnTimes(() -> handleDrawing(openId, seriesId), 10, 0);
        log.info("Draw is made as {}", draw);
        return draw;
    }

    public Draw confirmADraw(String drawId) {
        //TODO: update draw to confirm
        return null;
    }

    public Draw handleDrawing(String openId, String seriesId) {
        List<Product> productBySeries = productRepository.getProductBySeries(seriesId);
        Product selected = drawAProductBaseOnStock(productBySeries);
        if (selected.stock > 0L) {
            UUID drawID = UUID.randomUUID();
            selected.setStock(selected.getStock() - 1);
            selected.setVersion(selected.getVersion() + 1);
            Draw draw = new Draw(openId, drawID.toString(), DRAW_INIT_STATUS,
                    selected.getId(), seriesId, new Date());
            drawRepository.persistADraw(selected, draw);
            return draw;
        } else {
            throw new RuntimeException("No stock for selected product");
        }
    }

    public static Product drawAProductBaseOnStock(List<Product> productBySeries) {
        long sum = productBySeries.stream().mapToLong(Product::getStock).sum();
        double rand = Math.random();
        double cumulativeP = 0.0d;
        for (Product product : productBySeries) {
            cumulativeP += (double)product.stock/(double)sum;
            if (rand <= cumulativeP) {
                return product;
            }
        }
        throw new RuntimeException("Failed to draw a product");
    }
}
