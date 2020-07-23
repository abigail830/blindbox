package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Product;
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
    //Image f
    String productImage;


    String productGrayImage;

    BigDecimal price;


    public ProductDTO(Product product) {
        this.id = product.getId();
        this.seriesID = product.getSeriesID();
        this.name = product.getName();
        this.price = product.getPrice();
        this.productImage = Constant.WX_UI_IMAGE_PATH + product.getProductImage() + "?ts=" + System.currentTimeMillis()/1000;
        this.productGrayImage = Constant.WX_UI_IMAGE_PATH + product.getProductGrayImage() + "?ts=" + System.currentTimeMillis()/1000;

    }
}
