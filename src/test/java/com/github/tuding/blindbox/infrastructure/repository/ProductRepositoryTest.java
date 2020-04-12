package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@SpringBootTest
@DBRider
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;


    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductByName() {
        Optional<Product> productDTOOptional = productRepository.getProductByID("productid1");
        assertTrue(productDTOOptional.isPresent());
    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductBySeriesID() {
        List<Product> productBySeries = productRepository.getProductBySeries("seriesid1");
        assertThat(productBySeries.size(), is(3));
    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void getProductWithoutPriceByDrawId() {
        final Optional<Product> product = productRepository.getProductWithoutPriceByDrawID("d7493a65-1b22-479e-b3fe-decd80abe325");
        assertTrue(product.isPresent());
        assertEquals("product1", product.get().getName());
    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void getProductWithoutPriceByDrawId_not_paid() {
        final Optional<Product> product = productRepository.getProductWithoutPriceByDrawID("d7493a65-1b22-479e-b3fe-decd80abe326");
        assertFalse(product.isPresent());
    }


}
