package com.github.tuding.blindbox.domain.product;

import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.repository.DrawRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    SeriesRepository seriesRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DrawRepository drawRepository;

    public List<Role> getRoles() {
        return rolesRepository.queryRoles();
    }

    @Deprecated
    public List<Series> getSeriesListOld(String roleId) {
        return seriesRepository.queryByRoleIDOld(roleId);
    }

    public List<Series> getSeriesList(String roleId) {
        return seriesRepository.queryByRoleID(roleId);
    }

    public Optional<Series> getSeries(String seriesId) {
        return seriesRepository.querySeriesByIDWithRoleIds(seriesId);
    }

    @Deprecated
    public Optional<Series> getSeriesOld(String seriesId) {
        return seriesRepository.querySeriesByID(seriesId);
    }

    @Deprecated
    public List<Series> getAllNewSeriesOld() {
        return seriesRepository.queryAllNewSeriesOld();
    }

    public List<Series> getAllNewSeries() {
        return seriesRepository.queryAllNewSeries();
    }

    @Deprecated
    public List<Series> getAllSeriesOld(Integer limitPerPage, Integer numOfPage) {
        return seriesRepository.queryAllSeriesWithPagingOld(limitPerPage, numOfPage);
    }

    public List<Series> getAllSeries(Integer limitPerPage, Integer numOfPage) {
        return seriesRepository.queryAllSeriesWithPaging(limitPerPage, numOfPage);
    }

    public BigDecimal getProductPriceAfterDiscount(String drawId) {
        BigDecimal oriPrice = drawRepository.getPriceByDrawId(drawId);
        return oriPrice.multiply(Constant.DISCOUNT);
    }

    public List<Product> getProduct(String seriesId) {
        return productRepository.getProductBySeries(seriesId);
    }

    @Deprecated
    public List<Product> getProductWithPriceOld(String seriesId) {
        return productRepository.getProductWithPriceBySeriesIDOld(seriesId);
    }

    public List<Product> getProductWithPrice(String seriesId) {
        return productRepository.getProductWithPriceBySeriesID(seriesId);
    }

    public Product getProductWithoutPrice(String drawId) {
        return productRepository.getProductWithoutPriceByDrawID(drawId)
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<String> getBoughtProductIds(List<Product> products) {
        final List<String> ids = products.stream().map(Product::getId).collect(Collectors.toList());
        return productRepository.getProductIdWhichPayed(ids);
    }
}
