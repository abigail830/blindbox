package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DrawDTO {
    String drawId;
    String drawStatus;
    String seriesId;
    Date drawTime;
    String boxImage;
    BigDecimal price;
    String seriesName;
    Integer tipsCouponCost = Constant.GET_TIPS_COUPON_CONSUME_BONUS;
    Integer displayCouponCost = Constant.GET_DISPLAY_COUPON_CONSUME_BONUS;
    Integer discountCouponCost = Constant.GET_DISCOUNT_COUPON_CONSUME_BONUS;


    public DrawDTO (Draw draw) {
        this.drawId = draw.getDrawId();
        this.drawStatus = draw.getDrawStatus();
        this.seriesId = draw.getSeriesId();
        this.drawTime = draw.getDrawTime();
        this.boxImage = Constant.WX_UI_IMAGE_PATH + draw.getBoxImage();
        this.price = draw.getPrice();
        this.seriesName = draw.getSeriesName();
    }
}
