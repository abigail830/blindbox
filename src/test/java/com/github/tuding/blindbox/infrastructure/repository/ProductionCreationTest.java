package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@DBRider
public class ProductionCreationTest {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    SeriesRepository seriesRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DataSet("test-data/empty-product.yml")
    void createproducts() throws ParseException {
        Toggle.TEST_MODE.setStatus(true);

        System.out.println(formatter.parse("2020-03-13"));
        //roles
        rolesRepository.saveRole(new Role("roleid1", "testRole1",
                "/app/data/role/testRole1/testRole1.png"));

        rolesRepository.saveRole(new Role("roleid2", "testRole2",
                "/app/data/role/testRole2/testRole2.png"));

        assertTrue(rolesRepository.queryRolesByName("testRole1").isPresent());


        //series
        Series series1 = new Series("seriesid1",
                "testSeries", "2020-03-13", false, false, BigDecimal.valueOf(25.5),
                "/app/data/series/testSeries.png",
                "/app/data/series/header/testSeries.png",
                "/app/data/series/cell/testSeries.png",
                3,
                12,
                "/app/data/series/cell/longImage.png",
                "/app/data/series/cell/boxImage.png",
                "");
        series1.setLinkedRoleIds(Arrays.asList("testRole1"));
        seriesRepository.createSeriesV2(series1);
        seriesRepository.addSeriesRoleMappingV2(series1.getId(), series1.getLinkedRoleIds());

        Series series2 = new Series("seriesid2",
                "testSeries2", "2020-03-13", false, false, BigDecimal.valueOf(30),
                "/app/data/series/testSeries2.png",
                "/app/data/series/header/testSeries2.png",
                "/app/data/series/cell/testSeries2.png",
                3,
                12,
                "/app/data/series/cell/longImage.png",
                "/app/data/series/cell/boxImage.png",
                "");
        series2.setLinkedRoleIds(Arrays.asList("testRole1"));
        seriesRepository.createSeriesV2(series2);
        seriesRepository.addSeriesRoleMappingV2(series1.getId(), series1.getLinkedRoleIds());
        assertTrue(seriesRepository.querySeriesByNameWithoutRoleId("testSeries").isPresent());

        //product
        productRepository.createProduct(new Product("productid1",
                seriesRepository.querySeriesByNameWithoutRoleId("testSeries").get().getId(),
                "product1", false,
                200L,
                "/app/data/product/image/product1.png",
                "/app/data/product/image/grayproduct1.png",
                0L,
                null,
                200L));

        productRepository.createProduct(new Product("productid2",
                seriesRepository.querySeriesByNameWithoutRoleId("testSeries").get().getId(),
                "product2", false,
                200L,
                "/app/data/product/image/product2.png",
                "/app/data/product/image/grayproduct2.png",
                0L,
                null,
                200L));


        productRepository.createProduct(new Product("productid3", seriesRepository.querySeriesByNameWithoutRoleId("testSeries").get().getId(),
                "product3", true,
                10L,
                "/app/data/product/image/product3.png",
                "/app/data/product/image/grayproduct3.png",
                0L,
                null,
                200L));

        assertTrue(productRepository.getProductByID("productid1").isPresent());
        assertTrue(productRepository.getProductByID("productid2").isPresent());
        assertTrue(productRepository.getProductByID("productid3").isPresent());

    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductByName () {
        Optional<Product> productOptional = productRepository.getProductByID("productid1");
        assertTrue(productOptional.isPresent());
    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductBySeriesID () {
        List<Product> productBySeries = productRepository.getProductBySeries("seriesid1");
        assertThat(productBySeries.size(), is(3));
    }


}
