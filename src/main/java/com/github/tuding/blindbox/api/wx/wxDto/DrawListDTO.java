package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.domain.product.DrawList;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DrawListDTO {
    Map<Integer, DrawDTO> drawGroup;
    String drawListId;
    String seriesId;
    Date drawTime;
    String boxImage;
    BigDecimal price;
    String seriesName;
    Integer tipsCouponCost = Constant.GET_TIPS_COUPON_CONSUME_BONUS;
    Integer displayCouponCost = Constant.GET_DISPLAY_COUPON_CONSUME_BONUS;
    Integer discountCouponCost = Constant.GET_DISCOUNT_COUPON_CONSUME_BONUS;


    public DrawListDTO(DrawList drawList) {
        this.drawGroup = new HashMap<>();
        for (Map.Entry<Integer, Draw> entry : drawList.getDrawGroup().entrySet()) {
            drawGroup.put(entry.getKey(), new DrawDTO(entry.getValue()));
        }
        this.drawListId = drawList.getDrawListId();
        this.seriesId = drawList.getSeriesId();
        this.drawTime = drawList.getDrawTime();
        this.boxImage = Constant.WX_UI_IMAGE_PATH + drawList.getBoxImage();
        this.price = drawList.getPrice();
        this.seriesName = drawList.getSeriesName();
    }
}
