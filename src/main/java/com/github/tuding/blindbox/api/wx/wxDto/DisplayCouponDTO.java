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
public class DisplayCouponDTO {

    String productId;
    String productName;
    String productImage;
    String productGrayImage;
    BigDecimal price;

    String description;
    Integer remainBonus;

    public DisplayCouponDTO(Product excludedProduct, Integer remainBonus) {

        this.productId = excludedProduct.getId();
        this.productName = excludedProduct.getName();
        this.productImage = Constant.WX_UI_IMAGE_PATH + excludedProduct.getProductImage();
        this.productGrayImage = Constant.WX_UI_IMAGE_PATH + excludedProduct.getProductGrayImage();
        this.price = excludedProduct.getPrice();
        this.remainBonus = remainBonus;
        this.description = Constant.TIPS_COUPON_DESC;

    }
}
