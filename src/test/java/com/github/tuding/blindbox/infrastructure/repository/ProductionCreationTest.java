package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    SeriesRespository seriesRespository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DataSet("test-data/empty-product.yml")
    void createproducts() throws ParseException {
        Toggle.TEST_MODE.setStatus(true);

        System.out.println(formatter.parse("2020-03-13"));
        //roles
        rolesRepository.saveRole(new RoleDTO(1L, "testRole1", "test",
                "test", "/app/data/role/testRole1/testRole1.png"));

        rolesRepository.saveRole(new RoleDTO(2L, "testRole2", "test",
                "test", "/app/data/role/testRole2/testRole2.png"));

        assertTrue(rolesRepository.queryRolesByName("testRole1").isPresent());


        //series
        seriesRespository.createSeries(new SeriesDTO(1L, rolesRepository.queryRolesByName("testRole1").get().getId(),
                "testSeries", formatter.parse("2020-03-13"), false, false, BigDecimal.valueOf(25.5),
                "/app/data/series/testSeries.png", "/app/data/series/header/testSeries.png",
                "/app/data/series/cell/testSeries.png"));

        seriesRespository.createSeries(new SeriesDTO(2L, rolesRepository.queryRolesByName("testRole1").get().getId(),
                "testSeries2", formatter.parse("2020-03-13"), false, false, BigDecimal.valueOf(30),
                "/app/data/series/testSeries2.png", "/app/data/series/header/testSeries2.png",
                "/app/data/series/cell/testSeries2.png"));
        assertTrue(seriesRespository.querySeriesByName("testSeries").isPresent());

        //product
        productRepository.createProduct(new ProductDTO(1L, seriesRespository.querySeriesByName("testSeries").get().getId(),
                "product1", false, false,
                200,
                BigDecimal.valueOf(0.2),
                "/app/data/product/image/product1.png",
                "/app/data/product/postcard/product1.png"));

        productRepository.createProduct(new ProductDTO(1L, seriesRespository.querySeriesByName("testSeries").get().getId(),
                "product2", false, false,
                200,
                BigDecimal.valueOf(0.2),
                "/app/data/product/image/product2.png",
                "/app/data/product/postcard/product2.png"));


        productRepository.createProduct(new ProductDTO(1L, seriesRespository.querySeriesByName("testSeries").get().getId(),
                "product3", true, false,
                10,
                BigDecimal.valueOf(0.01),
                "/app/data/product/image/product3.png",
                "/app/data/product/postcard/product3.png"));

        assertTrue(productRepository.getProductByName("product1").isPresent());
        assertTrue(productRepository.getProductByName("product2").isPresent());
        assertTrue(productRepository.getProductByName("product3").isPresent());

    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductByName () {
        Optional<ProductDTO> productDTOOptional = productRepository.getProductByName("product1");
        assertTrue(productDTOOptional.isPresent());
    }

    @Test
    @DataSet("expect-data/save-product.yml")
    void queryProductBySeriesID () {
        List<ProductDTO> productBySeries = productRepository.getProductBySeries(1L);
        assertThat(productBySeries.size(), is(3));
    }


}
