package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.domain.product.DrawService;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.tuding.blindbox.domain.product.DrawService.drawAProductBaseOnStock;
import static com.github.tuding.blindbox.domain.product.DrawService.drawAProductBaseOnWeight;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DrawServiceTest {

    @Test
    void canDrawAProductWithProbability() {
        //P1->20% P2->30% P3->50%
        Product product1 = new Product();
        product1.setId("1");
        product1.setStock(20L);
        Product product2 = new Product();
        product2.setId("2");
        product2.setStock(30L);
        Product product3 = new Product();
        product3.setId("3");
        product3.setStock(50L);

        List<Product> products = Arrays.asList(product1, product2, product3);

        int product1Count = 0;
        int product2Count = 0;
        int product3Count = 0;

        //Draw
        for(int i=0; i<100000; i++) {

            Product product = drawAProductBaseOnStock(products);
            if (product.getId().equals("1")){
                product1Count ++;
            }

            if (product.getId().equals("2")){
                product2Count ++;
            }
            if (product.getId().equals("3")){
                product3Count ++;
            }
        }

        //Assert the statistic is similar to probability (i.e. within 1%)
        double product1Prob = (double)product1Count/100000;
        double product2Prob = (double)product2Count/100000;
        double product3Prob = (double)product3Count/100000;

        System.out.println(product1Prob);
        System.out.println(product2Prob);
        System.out.println(product3Prob);

        assertTrue(Math.abs(product1Prob - 0.2) < 0.01);
        assertTrue(Math.abs(product2Prob - 0.3) < 0.01);
        assertTrue(Math.abs(product3Prob - 0.5) < 0.01);
    }


    @Test
    void canDrawAProductWithWeight() {
        //P1->20% P2->30% P3->50%
        Product product1 = new Product();
        product1.setId("1");
        product1.setWeight(20L);
        Product product2 = new Product();
        product2.setId("2");
        product2.setWeight(30L);
        Product product3 = new Product();
        product3.setId("3");
        product3.setWeight(50L);

        List<Product> products = Arrays.asList(product1, product2, product3);

        int product1Count = 0;
        int product2Count = 0;
        int product3Count = 0;

        //Draw
        for(int i=0; i<100000; i++) {

            Product product = drawAProductBaseOnWeight(products);
            if (product.getId().equals("1")){
                product1Count ++;
            }

            if (product.getId().equals("2")){
                product2Count ++;
            }
            if (product.getId().equals("3")){
                product3Count ++;
            }
        }

        //Assert the statistic is similar to probability (i.e. within 1%)
        double product1Prob = (double)product1Count/100000;
        double product2Prob = (double)product2Count/100000;
        double product3Prob = (double)product3Count/100000;

        System.out.println(product1Prob);
        System.out.println(product2Prob);
        System.out.println(product3Prob);

        assertTrue(Math.abs(product1Prob - 0.2) < 0.01);
        assertTrue(Math.abs(product2Prob - 0.3) < 0.01);
        assertTrue(Math.abs(product3Prob - 0.5) < 0.01);
    }

    @Test
    void testExclusiveDrawing() {
        DrawRepository drawRepository = mock(DrawRepository.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("1", "series", "1", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("2", "series", "2", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("3", "series", "3", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("4", "series", "4", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("5", "series", "5", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("6", "series", "6", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("7", "series", "7", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("8", "series", "8", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("9", "series", "9", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("10", "series", "10", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("11", "series", "11", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("12", "series", "12", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("13", "series", "13", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("14", "series", "14", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("15", "series", "15", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("16", "series", "16", true, 100L,
                "","",0L, BigDecimal.valueOf(20), 10L));
        given(productRepository.getProductBySeries(anyString())).willReturn(productList);
        Series series = new Series();
        series.setTotalSize(12);
        DrawService drawService = new DrawService();
        drawService.setDrawRepository(drawRepository);
        drawService.setProductRepository(productRepository);
        List<Draw> draws = drawService.handleExclusiveDrawing("openID", "series",
                series, "testid");
        System.out.println(draws);
        Set<String> productIdSet = draws.stream().map(Draw::getProductId).collect(Collectors.toSet());
        assertThat(draws.size(), is(12));
        assertThat(productIdSet.size(), is(12));

        for (Draw draw : draws) {
            assertThat(draw.getProduct().getVersion(), is(1L));
            assertThat(draw.getProduct().getStock(), is(99L));
        }

    }

    @Test
    void testExclusiveDrawingWhenProductIsLessThan12() {
        DrawRepository drawRepository = mock(DrawRepository.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("1", "series", "1", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("2", "series", "2", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("3", "series", "3", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("4", "series", "4", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        given(productRepository.getProductBySeries(anyString())).willReturn(productList);
        Series series = new Series();
        series.setTotalSize(12);
        DrawService drawService = new DrawService();
        drawService.setDrawRepository(drawRepository);
        drawService.setProductRepository(productRepository);
        List<Draw> draws = drawService.handleExclusiveDrawing("openID", "series",
                series, "testid");
        System.out.println(draws);
        Set<String> productIdSet = draws.stream().map(Draw::getProductId).collect(Collectors.toSet());
        assertThat(draws.size(), is(4));
        assertThat(productIdSet.size(), is(4));
    }

    @Test
    void testExclusiveDrawingDoesNotDrawTheProductWhenItsWeightIsZero() {
        DrawRepository drawRepository = mock(DrawRepository.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("1", "series", "1", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("2", "series", "2", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("3", "series", "3", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("4", "series", "4", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("5", "series", "5", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("6", "series", "6", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("7", "series", "7", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("8", "series", "8", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("9", "series", "9", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("10", "series", "10", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 0L));
        productList.add(new Product("11", "series", "11", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("12", "series", "11", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        given(productRepository.getProductBySeries(anyString())).willReturn(productList);
        Series series = new Series();
        series.setTotalSize(12);
        DrawService drawService = new DrawService();
        drawService.setDrawRepository(drawRepository);
        drawService.setProductRepository(productRepository);
        List<Draw> draws = drawService.handleExclusiveDrawing("openID", "series",
                series, "testid");
        System.out.println(draws);
        Set<String> productIdSet = draws.stream().map(Draw::getProductId).collect(Collectors.toSet());
        assertThat(draws.size(), is(11));
        assertThat(productIdSet.size(), is(11));

        for (Draw draw : draws) {
            if (draw.getProductId().equalsIgnoreCase("10")) {
                Assert.fail();
            }
        }
    }

    @Test
    void testExclusiveDrawingDoesNotDrawTheProductWhenItsStockIsZero() {
        DrawRepository drawRepository = mock(DrawRepository.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("1", "series", "1", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("2", "series", "2", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("3", "series", "3", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("4", "series", "4", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("5", "series", "5", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("6", "series", "6", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("7", "series", "7", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("8", "series", "8", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("9", "series", "9", false, 100L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("10", "series", "10", false, 0L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("11", "series", "11", false, 0L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("12", "series", "11", false, 0L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        given(productRepository.getProductBySeries(anyString())).willReturn(productList);
        Series series = new Series();
        series.setTotalSize(12);
        DrawService drawService = new DrawService();
        drawService.setDrawRepository(drawRepository);
        drawService.setProductRepository(productRepository);
        List<Draw> draws = drawService.handleExclusiveDrawing("openID", "series",
                series, "testid");
        System.out.println(draws);
        System.out.println(draws.size());
        Set<String> productIdSet = draws.stream().map(Draw::getProductId).collect(Collectors.toSet());
        assertThat(draws.size(), is(9));
        assertThat(productIdSet.size(), is(9));

    }


    @Test
    void testExclusiveDrawingProbability() {
        DrawRepository drawRepository = mock(DrawRepository.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        List<Product> productList = new LinkedList<>();
        productList.add(new Product("1", "series", "1", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("2", "series", "2", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("3", "series", "3", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("4", "series", "4", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("5", "series", "5", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("6", "series", "6", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("7", "series", "7", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("8", "series", "8", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("9", "series", "9", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("10", "series", "10", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("11", "series", "11", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("12", "series", "12", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("13", "series", "13", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("14", "series", "14", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("15", "series", "15", false, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 100L));
        productList.add(new Product("16", "series", "16", true, 1000000L,
                "","",0L, BigDecimal.valueOf(20), 10L));
        given(productRepository.getProductBySeries(anyString())).willReturn(productList);
        Series series = new Series();
        series.setTotalSize(12);
        DrawService drawService = new DrawService();
        drawService.setDrawRepository(drawRepository);
        drawService.setProductRepository(productRepository);
        List<Draw> totalDraws = new LinkedList<>();
        List<Draw> draws = drawService.handleExclusiveDrawing("openID", "series",
                series, "testid");
        for(int i=0; i<10000; i++) {
            totalDraws.addAll(drawService.handleExclusiveDrawing("openID", "series",
                    series, "testid"));
        }


        System.out.println(totalDraws.size());
        List<String> selectedProduct = totalDraws.stream().map(Draw::getProductId).collect(Collectors.toList());
        System.out.println((double) Collections.frequency(selectedProduct, "15") / selectedProduct.size());
        System.out.println((double) Collections.frequency(selectedProduct, "16") / selectedProduct.size());
        assertTrue((double) Collections.frequency(selectedProduct, "16") / selectedProduct.size() < 0.012d);
        assertTrue((double) Collections.frequency(selectedProduct, "15") / selectedProduct.size() > 0.065d);

    }

}