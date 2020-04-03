package com.github.tuding.blindbox.domain.product;

import com.github.tuding.blindbox.exception.DrawNotFoundException;
import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import com.github.tuding.blindbox.infrastructure.util.RetryUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.github.tuding.blindbox.infrastructure.Constant.DRAW_INIT_STATUS;

@Service
@Slf4j
public class DrawService {
    //1 day
    public static long DRAW_TIMEOUT = 86400000;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    DrawRepository drawRepository;


    private final ScheduledExecutorService scheduledExecutorService
            = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("draw-auto-scan-thread-%d").build());

    public DrawService() {
        scheduledExecutorService.scheduleWithFixedDelay(this::deleteTimeoutDraw, 1, 24, TimeUnit.HOURS);
    }

    public void deleteTimeoutDraw() {
        log.info("Start to scan timeout draw. ");
        List<Draw> draws = drawRepository.getDraws();
        List<Draw> timeoutDraw = draws.stream()
                .filter(item -> System.currentTimeMillis() - item.getDrawTime().getTime() > DRAW_TIMEOUT)
                .collect(Collectors.toList());
        for (Draw draw : timeoutDraw) {
            log.info("Cancel the timeout draw {}", draw);
            cancelADraw(draw);
        }
    }

    public Draw drawAProduct(String openId, String seriesId) {
        log.info("Draw a product for {}", seriesId);
        cancelADrawByOpenID(openId);
        Optional<Series> series = seriesRespository.querySeriesByID(seriesId);
        Draw draw = RetryUtil.retryOnTimes(() -> handleDrawing(openId, seriesId, series.get()), 10, 0);
        log.info("Draw is made as {}", draw);
        return draw;
    }

    public void cancelADrawByOpenID(String openId) {
        log.info("Cancel a draw for {}", openId);
        Optional<Draw> drawOptional = drawRepository.getDrawByOpenID(openId);
        if (drawOptional.isPresent()) {
            cancelADraw(drawOptional.get());
        } else {
            log.warn("Can not find draw for openId ID {}", openId);
        }
    }

    private void cancelADraw(Draw draw) {
        Optional<Product> productOptional = productRepository.getProductByID(draw.productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setVersion(product.version + 1);
            product.setStock(product.stock + 1);
            drawRepository.cancelADraw(product, draw);
        } else {
            log.warn("Can not find product for product ID {}", draw.productId);
        }
    }


    public Draw getDrawByOpenID(String openId) {
        return drawRepository.getDrawByOpenID(openId).orElseThrow(DrawNotFoundException::new);
    }


    public Draw handleDrawing(String openId, String seriesId, Series series) {
        List<Product> productBySeries = productRepository.getProductBySeries(seriesId);
        Product selected = drawAProductBaseOnStock(productBySeries);
        if (selected.stock > 0L) {
            UUID drawID = UUID.randomUUID();
            selected.setStock(selected.getStock() - 1);
            selected.setVersion(selected.getVersion() + 1);
            Draw draw = new Draw(openId, drawID.toString(), DRAW_INIT_STATUS,
                    selected.getId(), seriesId, new Date(), series.price, series.boxImage,
                    selected.isSpecial, selected.productImage, series.name);
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

    public Product getExcludedProduct(String drawId) {
        Draw draw = drawRepository.getDrawByDrawID(drawId).get();
        List<Product> productBySeries = productRepository.getProductBySeries(draw.seriesId);
        List<Product> excludedList = productBySeries.stream()
                .filter(item -> !item.getId().equalsIgnoreCase(draw.productId))
                .collect(Collectors.toList());
        Random random = new Random();
        Product excludedProduct = excludedList.get(random.nextInt(excludedList.size()));
        excludedProduct.setPrice(draw.getPrice());
        return excludedProduct;
    }

    public Product getDrawProduct(String drawId) {
        Draw draw = drawRepository.getDrawByDrawID(drawId).get();
        Product productByID = productRepository.getProductByID(draw.productId).get();
        productByID.setPrice(draw.getPrice());
        return productByID;
    }
}
