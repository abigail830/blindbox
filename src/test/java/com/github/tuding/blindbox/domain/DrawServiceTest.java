package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tuding.blindbox.domain.product.DrawService.drawAProductBaseOnStock;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        for(int i=0; i<10000; i++) {

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
        double product1Prob = (double)product1Count/10000;
        double product2Prob = (double)product2Count/10000;
        double product3Prob = (double)product3Count/10000;

        System.out.println(product1Prob);
        System.out.println(product2Prob);
        System.out.println(product3Prob);

        assertTrue(Math.abs(product1Prob - 0.2) < 0.01);
        assertTrue(Math.abs(product2Prob - 0.3) < 0.01);
        assertTrue(Math.abs(product3Prob - 0.5) < 0.01);
    }

}