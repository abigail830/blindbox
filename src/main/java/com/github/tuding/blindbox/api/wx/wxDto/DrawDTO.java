package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Product;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DrawDTO {
    String drawId;
    String drawStatus;
    String productId;
    String seriesID;
    String name;
    Boolean isSpecial;
    //Image f
    String productImage;
    String postCardImage;

    public DrawDTO (String id, Product product) {
        this.drawId = id;
        this.drawStatus = "NEW";
        this.productId = product.getId();
        this.seriesID = product.getSeriesID();
        this.name = product.getName();
        this.isSpecial = product.getIsSpecial();
        this.productImage = Constant.WX_UI_IMAGE_PATH + product.getProductImage();
        this.postCardImage = Constant.WX_UI_IMAGE_PATH + product.getPostCardImage();
    }
}
