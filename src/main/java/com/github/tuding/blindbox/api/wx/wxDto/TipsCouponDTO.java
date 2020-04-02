package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TipsCouponDTO {

    String excludedProductId;
    String excludedProductName;
    String excludedProductImage;
    String excludedProductGrayImage;
    BigDecimal price;

    String description;
    Integer remainBonus;

    public TipsCouponDTO(Product excludedProduct, Integer remainBonus) {

        this.excludedProductId = excludedProduct.getId();
        this.excludedProductName = excludedProduct.getName();
        this.excludedProductImage = excludedProduct.getProductImage();
        this.excludedProductGrayImage = excludedProduct.getProductGrayImage();
        this.price = excludedProduct.getPrice();
        this.remainBonus = remainBonus;
        this.description = Constant.TIPS_COUPON_DESC;

    }
}
