package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.infrastructure.client.payment.SignUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Order {

    String orderId;
    Product product;
    String openId;
    String drawId;

    String prepayId;
    String nonceStr;
    String preOrderTime;
    String paySign;

    public Order(String orderId, Product product, String openId, String drawId) {
        this.orderId = orderId;
        this.product = product;
        this.openId = openId;
        this.drawId = drawId;
    }

    public void updateWxPayInfo(String prepayId, String nonceStr, String preOrderTime, String appId, String key) {
        this.prepayId = prepayId;
        this.nonceStr = nonceStr;
        this.preOrderTime = preOrderTime;
        sign(appId, key);
    }

    public void sign(String appid, String key) {
        String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepayId +
                "&signType=MD5&timeStamp=" + preOrderTime;
        this.paySign = SignUtil.sign(stringSignTemp, key, "utf-8").toUpperCase();
    }
}
