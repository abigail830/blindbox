package com.github.tuding.blindbox.domain.product;

import com.github.tuding.blindbox.exception.DrawNotFoundException;
import com.github.tuding.blindbox.infrastructure.repository.DrawListRepository;
import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRepository;
import com.github.tuding.blindbox.infrastructure.util.RetryUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.github.tuding.blindbox.infrastructure.Constant.DRAW_INIT_STATUS;

@Service
@Slf4j
public class DrawService {
    //1 hour
    public static long DRAW_TIMEOUT = 3600000;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SeriesRepository seriesRepository;

    @Autowired
    DrawRepository drawRepository;

    @Autowired
    DrawListRepository drawListRepository;


    private final ScheduledExecutorService scheduledExecutorService
            = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("draw-auto-scan-thread-%d").build());

    public DrawService() {
        scheduledExecutorService.scheduleWithFixedDelay(this::scanExpiredDraw, 1, 2, TimeUnit.HOURS);
    }

    public void scanExpiredDraw() {
        try {
            log.info("Start to scan timeout draw. ");
            List<Draw> draws = drawRepository.getDraws();
            List<Draw> timeoutDraw = draws.stream()
                    .filter(item -> item.getDrawStatus().equalsIgnoreCase(DRAW_INIT_STATUS))
                    .filter(item -> System.currentTimeMillis() - item.getDrawTime().getTime() > DRAW_TIMEOUT)
                    .collect(Collectors.toList());
            for (Draw draw : timeoutDraw) {
                log.info("Cancel the timeout draw {}", draw);
                cancelADraw(draw);
            }
        } catch (Exception ex) {
            log.error("Failed to handle draw scan. ", ex);
        }
    }

    @Deprecated
    public Draw drawAProductOld(String openId, String seriesId) {
        log.info("Draw a product for {}", seriesId);
        Optional<Series> series = seriesRepository.querySeriesByID(seriesId);
        Draw draw = RetryUtil.retryOnTimes(() -> handleDrawing(openId, seriesId, series.get()), 10, 0);
        log.info("Draw is made as {}", draw);
        return draw;
    }

    public Draw drawAProduct(String openId, String seriesId) {
        log.info("Draw a product for {}", seriesId);
        Optional<Series> series = seriesRepository.querySeriesByIDWithoutRoleIds(seriesId);
        Draw draw = RetryUtil.retryOnTimes(() -> handleDrawing(openId, seriesId, series.get()), 10, 0);
        log.info("Draw is made as {}", draw);
        return draw;
    }

    @Deprecated
    public void cancelADrawByOpenID(String openId) {
        log.info("Cancel a draw for {}", openId);
        Optional<Draw> drawOptional = drawRepository.getDrawByOpenID(openId);
        if (drawOptional.isPresent()) {
            cancelADraw(drawOptional.get());
        } else {
            log.warn("Can not find draw for openId ID {}", openId);
        }
    }

    public void cancelADrawByDrawId(String drawId) {
        log.info("Cancel a draw for draw Id {}", drawId);
        Optional<Draw> drawOptional = drawRepository.getDrawByDrawID(drawId);
        if (drawOptional.isPresent()) {
            cancelADraw(drawOptional.get());
        } else {
            log.warn("Can not find draw for draw ID {}", drawId);
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

    public Draw getDrawByDrawID(String drawId) {
        return drawRepository.getDrawByDrawID(drawId).orElseThrow(DrawNotFoundException::new);
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
                    selected.isSpecial, selected.productImage, series.name, selected);
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

    public void updateDrawPriceById(BigDecimal priceAfterDiscount, String drawId) {
        drawRepository.updateDrawPriceById(priceAfterDiscount, drawId);
    }

    public static Product drawAProductBaseOnWeight(List<Product> productBySeries) {
        long sum = productBySeries.stream().mapToLong(Product::getWeight).sum();
        double rand = Math.random();
        double cumulativeP = 0.0d;
        for (Product product : productBySeries) {
            cumulativeP += (double)product.weight/(double)sum;
            if (rand <= cumulativeP) {
                return product;
            }
        }
        return null;
    }

    public void persistDraw(Product selected, Draw draw) {
        try {
            selected.setStock(selected.getStock() - 1);
            selected.setVersion(selected.getVersion() + 1);
            drawRepository.persistADraw(selected, draw);
        } catch (Exception ex) {
            //Solve optimistic lock in concurrently mode
            RetryUtil.retryOnTimes(() -> {
                Product refresh = productRepository.getProductByID(selected.id).get();
                refresh.setStock(selected.getStock() - 1);
                refresh.setVersion(selected.getVersion() + 1);
                drawRepository.persistADraw(refresh, draw);
                return true;
            }, 10, 0);
        }
    }

    public static class DrawKV {
        public final Product product;
        public final Draw draw;

        public DrawKV(Product product, Draw draw) {
            this.product = product;
            this.draw = draw;
        }
    }

    public DrawKV exclusiveDraw(List<Product> productBySeries, String openId, String seriesId, Series series) {
        Product selected = drawAProductBaseOnWeight(productBySeries);
        if (selected != null && selected.stock > 0L) {
            UUID drawID = UUID.randomUUID();
            Draw draw = new Draw(openId, drawID.toString(), DRAW_INIT_STATUS,
                    selected.getId(), seriesId, new Date(), series.price, series.boxImage,
                    selected.isSpecial, selected.productImage, series.name, selected);
            return new DrawKV(selected, draw);
        } else if (selected != null){
            return new DrawKV(selected, null);
        } else {
            return null;
        }
    }

    public List<Draw> handleExclusiveDrawing(String openId, String seriesId, Series series, String drawListID) {
        List<Draw> res = new LinkedList<>();
        List<Product> productBySeries = productRepository.getProductBySeries(seriesId);
        String lastProductId = null;
        for (int count = 0; count < series.totalSize; count ++) {
            productBySeries = removeLastSelectedProduct(productBySeries, lastProductId);
            DrawKV drawkv = exclusiveDraw(productBySeries, openId, seriesId, series);
            if (drawkv != null) {
                log.info("draw product {} to drawListID {}", drawkv.product, drawListID);
                if (drawkv.draw != null) {
                    res.add(drawkv.draw);
                }
                lastProductId = drawkv.product.id;
            } else {
                lastProductId = null;
            }
        }
        return res;
    }

    private List<Product> removeLastSelectedProduct(List<Product> productBySeries, String finalLastProductId) {
        return productBySeries.stream()
                .filter(item -> !item.id.equalsIgnoreCase(finalLastProductId))
                .collect(Collectors.toList());
    }

    @Deprecated
    public DrawList drawAListOfProductOld(String openIdFromToken, String seriesId) {
        String drawListID = UUID.randomUUID().toString();
        log.info("Draw a list product for {} with drawList ID {}", seriesId, drawListID);
        Optional<Series> series = seriesRepository.querySeriesByIDWithoutRoleIds(seriesId);
        List<Draw> draws = handleExclusiveDrawing(openIdFromToken, seriesId, series.get(), drawListID);
        Map<Integer, Draw> drawGroup = new HashMap<>();
        for (int index = 0; index < draws.size(); index++) {
            drawGroup.put(index, draws.get(index));
        }
        DrawList drawList = new DrawList(openIdFromToken, drawListID, drawGroup, seriesId, new Date(),
                series.get().price, series.get().boxImage, series.get().name);
        log.info("Draw result {}", draws.stream().map(item -> item.product.name).collect(Collectors.toList()));
        log.info("Draw List is made as {}", drawList);
        drawListRepository.saveDrawList(drawList);
        return drawList;
    }

    public DrawList drawAListOfProduct(String openIdFromToken, String seriesId) {
        String drawListID = UUID.randomUUID().toString();
        log.info("Draw a list product for {} with drawList ID {}", seriesId, drawListID);
        Optional<Series> series = seriesRepository.querySeriesByIDWithoutRoleIds(seriesId);
        List<Draw> draws = handleExclusiveDrawing(openIdFromToken, seriesId, series.get(), drawListID);
        Map<Integer, Draw> drawGroup = new HashMap<>();
        for (int index = 0; index < draws.size(); index++) {
            drawGroup.put(index, draws.get(index));
        }
        DrawList drawList = new DrawList(openIdFromToken, drawListID, drawGroup, seriesId, new Date(),
                series.get().price, series.get().boxImage, series.get().name);
        log.info("Draw result {}", draws.stream().map(item -> item.product.name).collect(Collectors.toList()));
        log.info("Draw List is made as {}", drawList);
        drawRepository.saveDraw(draws);
        drawListRepository.saveDrawList(drawList);
        return drawList;
    }

    public DrawList getDrawList(String openIdFromToken, String drawListID) {
        return drawListRepository.getDrawList(drawListID).get();
    }

    public void cancelADrawListbyDrawListId(String drawListID) {
        DrawList drawList = drawListRepository.getDrawList(drawListID).get();
        log.info("Cancel draw list {}", drawList);
        for (Draw draw : drawList.getDrawGroup().values()) {
            if (draw != null && draw.getDrawStatus().equalsIgnoreCase(DRAW_INIT_STATUS)) {
                cancelADrawByDrawId(draw.getDrawId());
            }
        }
    }

    public void setDrawRepository(DrawRepository drawRepository) {
        this.drawRepository = drawRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
