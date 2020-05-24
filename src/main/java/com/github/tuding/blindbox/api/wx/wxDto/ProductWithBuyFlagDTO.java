package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ProductWithBuyFlagDTO extends ProductDTO {
    Boolean buy;

    public ProductWithBuyFlagDTO(Product product, Boolean buy) {
        this.id = product.getId();
        this.seriesID = product.getSeriesID();
        this.name = product.getName();
        this.price = product.getPrice();
        this.productImage = Constant.WX_UI_IMAGE_PATH + product.getProductImage();
        this.productGrayImage = Constant.WX_UI_IMAGE_PATH + product.getProductGrayImage();
        this.buy = buy;
    }
}
