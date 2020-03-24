package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Product;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

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
    Boolean isPresale;
    Long stock;
    BigDecimal probability;
    //Image f
    String productImage;

    String postCardImage;

    String productGrayImage;


    public ProductDTO(Product product) {
        this.id = product.getId();
        this.seriesID = product.getSeriesID();
        this.name = product.getName();
        this.isSpecial = product.getIsSpecial();
        this.isPresale = product.getIsPresale();
        this.stock = product.getStock();
        this.probability = product.getProbability();
        this.productImage = Constant.WX_UI_IMAGE_PATH + product.getProductImage();
        this.postCardImage = Constant.WX_UI_IMAGE_PATH + product.getPostCardImage();
        this.productGrayImage = Constant.WX_UI_IMAGE_PATH + product.getProductGrayImage();

    }
}
