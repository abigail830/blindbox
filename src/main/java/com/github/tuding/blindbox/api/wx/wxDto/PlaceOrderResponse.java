package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.order.Order;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderResponse {

    String prepayId;
    String nonceStr;
    String preOrderTime;
    String paySign;
    String signType = "MD5";
    String orderId;

    public PlaceOrderResponse(Order order) {
        this.prepayId = order.getPrepayId();
        this.nonceStr = order.getNonceStr();
        this.preOrderTime = order.getPreOrderTime();
        this.paySign = order.getPaySign();
        this.orderId = order.getOrderId();
    }
}
