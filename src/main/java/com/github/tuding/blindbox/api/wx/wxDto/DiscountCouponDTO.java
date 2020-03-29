package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCouponDTO {

    BigDecimal priceAfterDiscount;
    String description;
    Integer remainBonus;

    public DiscountCouponDTO(BigDecimal priceAfterDiscount, Integer remainBonus) {
        this.priceAfterDiscount = priceAfterDiscount;
        this.remainBonus = remainBonus;
        this.description = Constant.COUPON_DESC;

    }
}
