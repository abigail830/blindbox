package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@DBRider
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;


    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductByName () {
        Optional<ProductDTO> productDTOOptional = productRepository.getProductByID("productid1");
        assertTrue(productDTOOptional.isPresent());
    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductBySeriesID () {
        List<ProductDTO> productBySeries = productRepository.getProductBySeries("seriesid1");
        assertThat(productBySeries.size(), is(3));
    }



}
