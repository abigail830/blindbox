package com.github.tuding.blindbox.domain.product;

import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DrawRepository drawRepository;

    @Autowired

    public List<Role> getRoles() {
        return rolesRepository.queryRoles();
    }

    public List<Series> getSeriesList(String roleId) {
        return seriesRespository.queryByRoleID(roleId);
    }

    public Optional<Series> getSeries(String seriesId) {
        return seriesRespository.querySeriesByID(seriesId);
    }


    public List<Series> getAllNewSeries() {
        return seriesRespository.queryAllNewSeries();
    }

    public List<Series> getAllSeries(Integer limitPerPage, Integer numOfPage) {
        return seriesRespository.queryAllSeriesWithPaging(limitPerPage, numOfPage);
    }

    public BigDecimal getProductPriceAfterDiscount(String drawId) {
        BigDecimal oriPrice = drawRepository.getPriceByDrawId(drawId);
        return oriPrice.multiply(Constant.DISCOUNT);
    }

    public List<Product> getProduct(String seriesId) {
        return productRepository.getProductBySeries(seriesId);
    }

    public List<Product> getProductWithPrice(String seriesId) {
        return productRepository.getProductWithPriceBySeriesID(seriesId);
    }

    public Product getProductWithoutPrice(String drawId) {
        return productRepository.getProductWithoutPriceByDrawID(drawId)
                .orElseThrow(ProductNotFoundException::new);
    }
}
