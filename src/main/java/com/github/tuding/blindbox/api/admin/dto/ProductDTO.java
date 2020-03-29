package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Product;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ProductDTO {
    String id;
    String seriesID;
    String name;
    Boolean isSpecial;
    Long stock;
    BigDecimal probability;
    //Image f
    String productImage;
    MultipartFile productImageFile;

    String productGrayImage;
    MultipartFile productGrayImageFile;

    Long version;

    public Product toDomainObject() {
        Product product = new Product();
        product.setId(id);
        product.setSeriesID(seriesID);
        product.setName(name);
        product.setIsSpecial(isSpecial);
        product.setStock(stock);
        product.setProbability(probability);
        product.setProductImage(productImage);
        product.setProductGrayImage(productGrayImage);
        product.setVersion(version);
        return product;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.seriesID = product.getSeriesID();
        this.name = product.getName();
        this.isSpecial = product.getIsSpecial();
        this.stock = product.getStock();
        this.probability = product.getProbability();
        this.version = product.getVersion();
        this.productImage = Constant.ADMIN_UI_IMAGE_PATH + product.getProductImage();
        this.productGrayImage = Constant.ADMIN_UI_IMAGE_PATH + product.getProductGrayImage();

    }
}
